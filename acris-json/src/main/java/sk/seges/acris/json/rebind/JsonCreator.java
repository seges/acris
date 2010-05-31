package sk.seges.acris.json.rebind;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import sk.seges.acris.core.rebind.RebindUtils;
import sk.seges.acris.core.rebind.RebindUtils.FieldDeclaration;
import sk.seges.acris.json.client.PrimitiveJsonizer;
import sk.seges.acris.json.client.annotation.Field;
import sk.seges.acris.json.client.context.DeserializationContext;
import sk.seges.acris.json.client.data.IJsonObject;
import sk.seges.acris.json.client.deserialization.JsonDeserializer;
import sk.seges.acris.json.rebind.util.AnnotationHelper;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.ext.GeneratorContext;
import com.google.gwt.core.ext.TreeLogger;
import com.google.gwt.core.ext.UnableToCompleteException;
import com.google.gwt.core.ext.TreeLogger.Type;
import com.google.gwt.core.ext.typeinfo.JClassType;
import com.google.gwt.core.ext.typeinfo.JField;
import com.google.gwt.core.ext.typeinfo.JParameterizedType;
import com.google.gwt.core.ext.typeinfo.JType;
import com.google.gwt.core.ext.typeinfo.NotFoundException;
import com.google.gwt.core.ext.typeinfo.TypeOracle;
import com.google.gwt.json.client.JSONArray;
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
			type = context.getTypeOracle().getType(IJsonObject.class.getName());
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, "Cannot find class", e);
			throw new UnableToCompleteException();
		}

		String className = getResultSuffix();

		SourceWriter sourceWriter = getSourceWriter(packageName, className);

		JClassType[] types = type.getSubtypes();

		sourceWriter.println("public " + Object.class.getSimpleName() + " fromJson(" + JSONValue.class.getSimpleName()
				+ " jsonValue, " + Class.class.getSimpleName()
				+ " clazz, DeserializationContext deserializationContext) {");
		sourceWriter.indent();

		for (JClassType JSONclassType : types) {
			generateDeserialize(sourceWriter, JSONclassType);
		}

		sourceWriter.println("return super.fromJson(jsonValue, clazz, deserializationContext);");

		sourceWriter.outdent();
		sourceWriter.println("}");

		sourceWriter.println("public boolean supports(" + JSONValue.class.getSimpleName() + " jsonValue, "
				+ Class.class.getSimpleName() + " clazz) {");
		sourceWriter.indent();

		sourceWriter.println("if (super.supports(jsonValue, clazz)) {");
		sourceWriter.indent();
		sourceWriter.println("return true;");
		sourceWriter.outdent();
		sourceWriter.println("}");

		for (JClassType JSONclassType : types) {
			generateSupports(sourceWriter, JSONclassType);
		}

		sourceWriter.println("return false;");

		sourceWriter.outdent();
		sourceWriter.println("}");

		sourceWriter.commit(logger);

		return packageName + "." + className;
	}

	protected String getResultSuffix() {
		return "Jsonizer";
	}

	protected boolean validateFieldDeclaration(FieldDeclaration fd) {
		if (!fd.isPublic && fd.setterMethod == null) {
			return false;
		}

		if (fd.type == null) {
			return false;
		}

		return true;
	}

	protected void generateField(SourceWriter sourceWriter, JClassType toType, JField field)
			throws UnableToCompleteException {
		Field fieldAnnotation = field.getAnnotation(Field.class);

		if (fieldAnnotation == null) {
			return;
		}

		FieldDeclaration fd;

		try {
			fd = RebindUtils.getFieldDeclaration(toType, field.getName());
		} catch (NotFoundException e) {
			logger.log(Type.ERROR, "Unable to find setter for field " + field.getName() + " for bean "
					+ toType.getName(), e);
			throw new UnableToCompleteException();
		}

		if (!validateFieldDeclaration(fd)) {
			logger.log(Type.INFO, "Field " + field.getName() + " in " + toType.getName()
					+ " has not valid field definition");
			return;
		}

		String fieldName = fieldAnnotation.value();

		if (fieldName == null || fieldName.length() == 0) {
			fieldName = field.getName();
		}

		sourceWriter.println("deserializationContext = new " + DeserializationContext.class.getSimpleName() + "();");

		if (fd.annotations != null) {
			for (Annotation annotation : fd.annotations) {
				Map<String, String> param = AnnotationHelper.getMembers(annotation);
				if (param != null) {
					for (Entry<String, String> paramEntry : param.entrySet()) {
						sourceWriter.println("deserializationContext.putAttribute(\"" + paramEntry.getKey() + "\",\""
								+ paramEntry.getValue() + "\");");
					}
				}
			}
		}

		sourceWriter.println("deserializationContext.setJsonizer(this);");

		if (fd.type.isArray() != null) {

		} else if (fd.type.isClassOrInterface() != null
				&& fd.type.isClassOrInterface().isAssignableTo(typeOracle.findType(Collection.class.getName()))) {

			sourceWriter.println(JSONValue.class.getSimpleName() + " jsonValue" + field.getName()
					+ " = jsonObject.get(\"" + fieldName + "\");");
			sourceWriter.println("if (jsonValue" + field.getName() + " != null) {");
			sourceWriter.indent();

			JType targetType = null;

			sk.seges.acris.json.client.annotation.Type type = field
					.getAnnotation(sk.seges.acris.json.client.annotation.Type.class);

			if (type != null && type.value() != null) {
				targetType = typeOracle.findType(type.value().getName());
			}

			if (targetType == null) {
				JParameterizedType parametrizedType = fd.type.isParameterized();
				if (parametrizedType != null && parametrizedType.getTypeArgs() != null
						&& parametrizedType.getTypeArgs().length == 1) {
					targetType = parametrizedType.getTypeArgs()[0];
				}
			}

			if (targetType != null) {
				sourceWriter.println(Class.class.getSimpleName() + "<?> targetClazz = " + targetType.getQualifiedSourceName()
						+ ".class;");
			} else {
				sourceWriter.println(Class.class.getSimpleName() + "<?> targetClazz = jsonizerContext.getPropertyType("
						+ toType.getQualifiedSourceName() + ".class, \"" + fieldName + "\");");
			}

			sourceWriter.println("if (targetClazz != null) {");
			sourceWriter.indent();

			sourceWriter.println(JSONArray.class.getSimpleName() + " jsonArray = jsonValue" + field.getName()
					+ ".isArray();");
			sourceWriter.println("if (jsonArray != null) {");
			sourceWriter.indent();

			generateSetter(sourceWriter, fd, field, "_fromJsonToCollection(jsonArray, targetClazz, "
					+ Collection.class.getName() + ".class, null, deserializationContext)");
			sourceWriter.outdent();
			sourceWriter.println("} else {");
			sourceWriter.indent();
			generateSetter(sourceWriter, fd, field, "(" + Collection.class.getName()
					+ ")fromJson(jsonValue" + field.getName() + ", targetClazz, deserializationContext)");
			sourceWriter.outdent();
			sourceWriter.println("}");
			sourceWriter.outdent();
			sourceWriter.println("} else {");
			sourceWriter.indent();
			generateSetter(sourceWriter, fd, field, "null");
			sourceWriter.println("}");
			sourceWriter.outdent();
			sourceWriter.println("} else {");
			generateSetter(sourceWriter, fd, field, "null");
			sourceWriter.println("}");
		} else {
			generateSetter(sourceWriter, fd, field, "(" + field.getType().getQualifiedSourceName()
					+ ")fromJson(jsonObject.get(\"" + fieldName + "\"), " + field.getType().getQualifiedSourceName()
					+ ".class, deserializationContext)");
		}
	}

	protected void generateSetter(SourceWriter sourceWriter, FieldDeclaration fd, JField field, String set) {
		if (fd.isPublic) {
			sourceWriter.println("data." + field.getName() + " = " + set + ";");
		} else {
			sourceWriter.println("data." + fd.setterMethod.getName() + "(" + set + ");");
		}
	}

	protected void generateSupports(SourceWriter sourceWriter, JClassType classType) throws UnableToCompleteException {
		JClassType toType;

		try {
			toType = RebindUtils.getGenericsFromInterfaceType(classType, typeOracle.findType(IJsonObject.class
					.getName()), 0);
		} catch (NotFoundException e) {
			logger.log(Type.ERROR, "Unable to extract generic type class from interface");
			throw new UnableToCompleteException();
		}

		sourceWriter.println("if (jsonValue.isObject() != null  && clazz.getName().equals("
				+ toType.getQualifiedSourceName() + ".class.getName())) {");
		sourceWriter.indent();
		sourceWriter.println("return true;");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	protected void generateDeserialize(SourceWriter sourceWriter, JClassType classType)
			throws UnableToCompleteException {

		JClassType toType;

		try {
			toType = RebindUtils.getGenericsFromInterfaceType(classType, typeOracle.findType(IJsonObject.class
					.getName()), 0);
		} catch (NotFoundException e) {
			logger.log(Type.ERROR, "Unable to extract generic type class from interface");
			throw new UnableToCompleteException();
		}

		sourceWriter.println("if (jsonValue.isObject() != null && clazz.getName().equals("
				+ toType.getQualifiedSourceName() + ".class.getName())) {");
		sourceWriter.indent();

		// Using deserializator
		sourceWriter.println(JsonDeserializer.class.getSimpleName() + "<" + toType.getQualifiedSourceName() + ", "
				+ JSONValue.class.getSimpleName() + "> deserializer = jsonizerContext." + "getDeserializer("
				+ toType.getQualifiedSourceName() + ".class);");

		sourceWriter.println("if (deserializer != null) {");
		sourceWriter.indent();
		sourceWriter.println("return deserializer.deserialize(jsonValue, deserializationContext);");
		sourceWriter.outdent();
		sourceWriter.println("}");

		sourceWriter.println("");
		sourceWriter.println("JSONObject jsonObject = jsonValue.isObject();");

		sourceWriter
				.println(toType.getQualifiedSourceName() + " data = new " + toType.getQualifiedSourceName() + "();");

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

		sourceWriter.println("return data;");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	protected String[] getImports() {
		return new String[] { GWT.class.getCanonicalName(), JSONArray.class.getCanonicalName(),
				JSONValue.class.getCanonicalName(), JSONObject.class.getCanonicalName(),
				JsonDeserializer.class.getCanonicalName(), Map.class.getCanonicalName(),
				DeserializationContext.class.getCanonicalName(), HashMap.class.getCanonicalName(), };
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

		composerFactory.setSuperclass(PrimitiveJsonizer.class.getCanonicalName());

		return composerFactory.createSourceWriter(context, printWriter);
	}
}