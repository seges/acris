package sk.seges.acris.json.rebind;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import sk.seges.acris.core.rebind.RebindUtils;
import sk.seges.acris.core.rebind.RebindUtils.FieldDeclaration;
import sk.seges.acris.json.client.IJsonizer;
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
			type = context.getTypeOracle().getType(IJsonObject.class.getName());
		} catch (NotFoundException e) {
			logger.log(TreeLogger.ERROR, "Cannot find class", e);
			throw new UnableToCompleteException();
		}

		String className = getResultSuffix();

		SourceWriter sourceWriter = getSourceWriter(packageName, className);

		JClassType[] types = type.getSubtypes();

		sourceWriter.println("public " + Object.class.getSimpleName() + " fromJson(" + JSONValue.class.getSimpleName()
				+ " jsonValue, " + Class.class.getSimpleName() + " clazz, DeserializationContext deserializationContext) {");
		sourceWriter.indent();

		for (JClassType JSONclassType : types) {
			generateDeserialize(sourceWriter, JSONclassType);
		}

		sourceWriter.println("return null;");

		sourceWriter.outdent();
		sourceWriter.println("}");
		
		sourceWriter.println("public boolean supports(" + JSONValue.class.getSimpleName() + " jsonValue, " + Class.class.getSimpleName() + " clazz) {");
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
						sourceWriter.println("deserializationContext.putAttribute(\""
								+ paramEntry.getKey() + "\",\"" + paramEntry.getValue() + "\");");
					}
				}
			}
		}

		sourceWriter.println("deserializationContext.setJsonizer(this);");

		if (fd.isPublic) {
			sourceWriter.println("data." + field.getName() + " = (" + field.getType().getQualifiedSourceName()
					+ ")fromJson(jsonObject.get(\"" + fieldName + "\"), " + field.getType().getQualifiedSourceName()
					+ ".class, deserializationContext);");
		} else {
			sourceWriter.println("data." + fd.setterMethod.getName() + "((" + field.getType().getQualifiedSourceName()
					+ ")fromJson(jsonObject.get(\"" + fieldName + "\"), " + field.getType().getQualifiedSourceName()
					+ ".class, deserializationContext));");
		}
	}

	// protected void finalizeEmptyField(JClassType toType, JField field, FieldDeclaration fd) {
	// if (fd.isPublic) {
	// sourceWriter.println("((" + toType.getQualifiedSourceName() + ")t)." + field.getName() +
	// " = convertResultType(_result, " + toType.getName() + ".class);");
	// } else {
	// sourceWriter.println("((" + toType.getQualifiedSourceName() + ")t)." + fd.setterMethod.getName()
	// + "(convertResultType(_result, " + toType.getName() + ".class));");
	// }
	// }

	// protected boolean generateEmptyField(SourceWriter sourceWriter, JClassType toType, JClassType resultType,
	// JField field, FieldDeclaration fd) {
	//		
	// if (!fd.isPublic && fd.getterMethod == null) {
	// return false;
	// }
	//
	// sourceWriter.println(resultType.getName() + " _result;");
	//		
	// if (fd.isPublic) {
	// sourceWriter.println("if (((" + toType.getQualifiedSourceName() + ")t)." + field.getName() + " == null) {");
	// } else {
	// sourceWriter.println("if (((" + toType.getQualifiedSourceName() + ")t)." + fd.getterMethod.getName()
	// + "() == null) {");
	// }
	//
	// sourceWriter.indent();
	// sourceWriter.println("");
	//
	// sourceWriter.println("_result =  + instantiateField(" + resultType.getName() + ".class);");
	//		
	// sourceWriter.outdent();
	// sourceWriter.println("}");
	//		
	// return true;
	// }
	//
	// protected boolean handleArrayField(SourceWriter sourceWriter, JClassType toType, JField field, FieldDeclaration
	// fd)
	// throws UnableToCompleteException {
	// try {
	// generateEmptyField(sourceWriter, toType, typeOracle.getType(ArrayList.class.getName()), field, fd);
	//			
	// sourceWriter.println("
	// } catch (NotFoundException e) {
	// logger.log(Type.ERROR,
	// "ArrayList class could not be found on the classpath. Do not try to fix that, it's all broken and non repairable.");
	// throw new UnableToCompleteException();
	// }
	// return false;
	// }

	// protected boolean handleCollectionField(SourceWriter sourceWriter, JClassType toType, JField field,
	// FieldDeclaration fd) throws UnableToCompleteException {
	// return false;
	// }
	//
	// protected boolean handleMapField(SourceWriter sourceWriter, JClassType toType, JField field, FieldDeclaration fd)
	// throws UnableToCompleteException {
	// return false;
	// }

	// protected boolean handleIterableFields(SourceWriter sourceWriter, JClassType toType, JField field,
	// FieldDeclaration fd) throws UnableToCompleteException {
	//
	// JSONArray jsonArray = jsonValue.isArray();
	//		
	// if (fd.type.isArray() != null) {
	// return handleArrayField(sourceWriter, toType, field, fd);
	// } else if (fd.type.isClass() != null) {
	// try {
	// if (fd.type.isClass().isAssignableFrom(typeOracle.getType(Collection.class.getName()))) {
	// return handleCollectionField(sourceWriter, toType, field, fd);
	// } else if (fd.type.isClass() != null
	// && fd.type.isClass().isAssignableFrom(typeOracle.getType(Map.class.getName()))) {
	// return handleMapField(sourceWriter, toType, field, fd);
	// }
	// } catch (NotFoundException e) {
	// logger.log(Type.ERROR,
	// "No Collection or/and Map interface is on the classpath. Something mystical happens.");
	// throw new UnableToCompleteException();
	// }
	// }
	//
	// return false;
	// }

	protected void generateSupports(SourceWriter sourceWriter, JClassType classType) throws UnableToCompleteException {
		JClassType toType;

		try {
			toType = RebindUtils.getGenericsFromInterfaceType(classType,
					typeOracle.findType(IJsonObject.class.getName()), 0);
		} catch (NotFoundException e) {
			logger.log(Type.ERROR, "Unable to extract generic type class from interface");
			throw new UnableToCompleteException();
		}
		
		sourceWriter.println("if (jsonValue.isObject() != null  && clazz.getName().equals(" + toType.getQualifiedSourceName() + ".class.getName())) {");
		sourceWriter.indent();
		sourceWriter.println("return true;");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}
	
	protected void generateDeserialize(SourceWriter sourceWriter, JClassType classType) throws UnableToCompleteException {

		JClassType toType;

		try {
			toType = RebindUtils.getGenericsFromInterfaceType(classType,
					typeOracle.findType(IJsonObject.class.getName()), 0);
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

		sourceWriter.println(toType.getQualifiedSourceName() + " data = new " + toType.getQualifiedSourceName() + "();");

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
		return new String[] { GWT.class.getCanonicalName(), JSONValue.class.getCanonicalName(),
				JSONObject.class.getCanonicalName(), JsonDeserializer.class.getCanonicalName(),
				Map.class.getCanonicalName(), DeserializationContext.class.getCanonicalName(),
				HashMap.class.getCanonicalName(), };
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
		// composerFactory.addImplementedInterface(IJsonizer.class.getCanonicalName());

		return composerFactory.createSourceWriter(context, printWriter);
	}
}