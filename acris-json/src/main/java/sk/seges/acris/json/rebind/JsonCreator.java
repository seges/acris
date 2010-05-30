package sk.seges.acris.json.rebind;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import sk.seges.acris.core.rebind.RebindUtils;
import sk.seges.acris.core.rebind.RebindUtils.FieldDeclaration;
import sk.seges.acris.json.client.IJsonizer;
import sk.seges.acris.json.client.JsonizerProvider;
import sk.seges.acris.json.client.annotation.Field;
import sk.seges.acris.json.client.provider.JsonAdapter;
import sk.seges.acris.json.client.provider.JsonDataAdapterProvider;
import sk.seges.acris.json.rebind.util.AnnotationHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.json.client.JSONObject;
import com.google.gwt.json.client.JSONValue;
import com.google.gwt.user.rebind.ClassSourceFileComposerFactory;
import com.google.gwt.user.rebind.SourceWriter;

public class JsonCreator {

	private TypeOracle typeOracle;

	private String packageName;

	private TreeLogger logger;

	private GeneratorContext context;

	public String generate(TreeLogger logger, GeneratorContext context, String typeName)
			throws UnableToCompleteException {
		this.typeOracle = context.getTypeOracle();
		this.logger = logger;
		this.context = context;

		assert typeOracle != null;

		JClassType classType = null;
		try {
			classType = typeOracle.getType(typeName);
		} catch (NotFoundException e) {
			logger.log(Type.ERROR, "Unable to find classtype for " + typeName);
			throw new UnableToCompleteException();
		}

		packageName = classType.getPackage().getName();

		JClassType type = null;
		try {
			type = context.getTypeOracle().getType(IJsonizer.class.getName());
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, "Cannot find class", e);
			throw new UnableToCompleteException();
		}

		String className = getResultSuffix();

		SourceWriter sourceWriter = getSourceWriter(packageName, className);

		JClassType[] types = type.getSubtypes();

		sourceWriter.println("public boolean jsonize(" + JSONValue.class.getSimpleName() + " jsonValue, Object t) {");
		sourceWriter.indent();

		sourceWriter.println("boolean result = false;");

		for (JClassType JSONclassType : types) {
			generate(sourceWriter, JSONclassType);
		}

		sourceWriter.println("return result;");

		sourceWriter.outdent();
		sourceWriter.println("}");

		sourceWriter.commit(logger);

		return packageName + "." + className;
	}

	protected String getResultSuffix() {
		return "Jsonizer";
	}

	protected void generateField(SourceWriter sourceWriter, JClassType toType, JField field)
			throws UnableToCompleteException {
		Field fieldAnnotation = field.getAnnotation(Field.class);

		if (fieldAnnotation != null) {

			FieldDeclaration fd;

			try {
				fd = RebindUtils.getSetter(toType, field.getName());
			} catch (NotFoundException e) {
				logger.log(Type.ERROR, "Unable to find setter for field " + field.getName() + " for bean "
						+ toType.getName(), e);
				throw new UnableToCompleteException();
			}

			String fieldName = fieldAnnotation.value();

			if (fieldName == null || fieldName.length() == 0) {
				fieldName = field.getName();
			}

			String typeName = fd.type.getQualifiedSourceName();

			sourceWriter.println(Map.class.getSimpleName() + "<" + String.class.getSimpleName() + ", "
					+ String.class.getSimpleName() + "> params" + field.getName() + " = new "
					+ HashMap.class.getCanonicalName() + "<" + String.class.getSimpleName() + ", "
					+ String.class.getSimpleName() + ">();");

			if (fd.annotations != null) {
				for (Annotation annotation : fd.annotations) {
					Map<String, String> param = AnnotationHelper.getMembers(annotation);
					if (param != null) {
						for (Entry<String, String> paramEntry : param.entrySet()) {
							sourceWriter.println("params" + field.getName() + ".put(\"" + paramEntry.getKey() + "\",\""
									+ paramEntry.getValue() + "\");");
						}
					}
				}
			}

			sourceWriter.println(JsonAdapter.class.getSimpleName() + "<" + typeName + ", "
					+ JSONValue.class.getSimpleName() + "> adapter" + field.getName() + " = "
					+ JsonDataAdapterProvider.class.getName() + ".getAdapter(" + typeName + ".class);");
			sourceWriter.println("if (adapter" + field.getName() + " != null) {");
			sourceWriter.indent();
			if (fd.isPublic) {
				sourceWriter.println("((" + toType.getQualifiedSourceName() + ")t)." + field.getName() + " = adapter"
						+ field.getName() + ".setValue(jsonValue, \"" + fieldName + "\", params" + field.getName()
						+ ");");
			} else {
				sourceWriter.println("((" + toType.getQualifiedSourceName() + ")t)." + fd.setterMethod.getName()
						+ "(adapter" + field.getName() + ".setValue(jsonValue, \"" + fieldName + "\", params"
						+ field.getName() + "));");
			}

			sourceWriter.outdent();
			if (fd.type.isPrimitive() != null) {
				sourceWriter.println("}");
			} else {
				sourceWriter.println("} else if (jsonValue.isObject() != null) { ");
				sourceWriter.indent();
				sourceWriter.println("JSONObject jsonObject = jsonValue.isObject();");
				sourceWriter.println("IJsonizer<" + fd.type.getQualifiedSourceName() + "> jsonnizer = "
						+ GWT.class.getSimpleName() + ".create(" + JsonizerProvider.class.getCanonicalName()
						+ ".class);");
				sourceWriter.println(fd.type.getQualifiedSourceName() + " data = new "
						+ fd.type.getQualifiedSourceName() + "();");
				if (fd.isPublic) {
					sourceWriter
							.println("((" + toType.getQualifiedSourceName() + ")t)." + field.getName() + " = data;");
				} else {
					sourceWriter.println("((" + toType.getQualifiedSourceName() + ")t)." + fd.setterMethod.getName()
							+ "(data);");
				}
				sourceWriter.println("jsonnizer.jsonize(jsonObject.get(\"" + fieldName + "\"), data);");
				sourceWriter.outdent();
				sourceWriter.println("}");
			}
		}
	}

	protected void generate(SourceWriter sourceWriter, JClassType classType) throws UnableToCompleteException {

		JClassType toType;

		try {
			toType = RebindUtils.getGenericsFromInterfaceType(classType,
					typeOracle.findType(IJsonizer.class.getName()), 0);
		} catch (NotFoundException e) {
			logger.log(Type.ERROR, "Unable to extract generic type class from interface");
			throw new UnableToCompleteException();
		}

		sourceWriter.println("if (t instanceof " + toType.getQualifiedSourceName() + ") { ");
		sourceWriter.indent();

		sourceWriter.println("result = true;");

		for (JField field : toType.getFields()) {
			generateField(sourceWriter, toType, field);
		}

		toType = toType.getSuperclass();

		while (toType != null) {
			for (JField field : toType.getFields()) {
				generateField(sourceWriter, toType, field);
			}

			toType = toType.getSuperclass();
		}

		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	protected String[] getImports() {
		return new String[] { GWT.class.getCanonicalName(), JSONValue.class.getCanonicalName(),
				JsonDataAdapterProvider.class.getCanonicalName(), JSONObject.class.getCanonicalName(),
				JsonAdapter.class.getCanonicalName(), Map.class.getCanonicalName(), HashMap.class.getCanonicalName(), };
	}

	protected SourceWriter getSourceWriter(String packageName, String beanClassName) {
		PrintWriter printWriter = context.tryCreate(logger, packageName, beanClassName);

		if (printWriter == null) {
			return null;
		}

		ClassSourceFileComposerFactory composerFactory = new ClassSourceFileComposerFactory(packageName, beanClassName);

		for (String importName : getImports()) {
			composerFactory.addImport(importName);
		}

		composerFactory.addImplementedInterface(IJsonizer.class.getCanonicalName());

		return composerFactory.createSourceWriter(context, printWriter);
	}
}