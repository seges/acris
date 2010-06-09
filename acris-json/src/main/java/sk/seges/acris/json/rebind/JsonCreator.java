package sk.seges.acris.json.rebind;

import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import sk.seges.acris.core.rebind.RebindUtils;
import sk.seges.acris.core.rebind.RebindUtils.FieldDeclaration;
import sk.seges.acris.json.client.ExtendableJsonizer;
import sk.seges.acris.json.client.annotation.ComplexField;
import sk.seges.acris.json.client.annotation.Field;
import sk.seges.acris.json.client.annotation.JsonObject;
import sk.seges.acris.json.client.context.DeserializationContext;
import sk.seges.acris.json.client.data.IJsonObject;
import sk.seges.acris.json.client.deserialization.JsonDeserializer;
import sk.seges.acris.json.client.extension.ExtensionPoint;
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

		if (sourceWriter == null) {
			return packageName + "." + className;
		}

		JClassType[] types = type.getSubtypes();

		//Pointer name Resolver
		sourceWriter.println("protected String getDefaultPointName(" + Class.class.getSimpleName() + "<?> type) {");
		sourceWriter.indent();
		for (JClassType JSONclassType : types) {
			generatePointNameResolver(sourceWriter, JSONclassType);
		}
		sourceWriter.println("return null;");
		sourceWriter.outdent();
		sourceWriter.println("}");

		sourceWriter.println("public " + Object.class.getSimpleName() + " fromJson(" + JSONValue.class.getSimpleName()
				+ " jsonValue, " + Class.class.getSimpleName()
				+ " clazz, DeserializationContext deserializationContext) {");
		sourceWriter.indent();
		sourceWriter.println("if (jsonValue == null) {");
		sourceWriter.indent();
		sourceWriter.println("return null;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		
		// Using deserializator
		sourceWriter.println(JsonDeserializer.class.getSimpleName() + "<?, " + JSONValue.class.getSimpleName()
				+ "> deserializer = jsonizerContext." + "getDeserializer(clazz);");

		sourceWriter.println("if (deserializer != null) {");
		sourceWriter.indent();
		sourceWriter.println("Object result = deserializer.deserialize(jsonValue, deserializationContext);");
		sourceWriter.println("");
		
		sourceWriter.println("if (result != null && (result instanceof " + ExtensionPoint.class.getSimpleName() + ") && jsonValue != null && jsonValue.isObject() != null) {");
		sourceWriter.indent();
		sourceWriter.println("return fromJson(jsonValue.isObject(), clazz, (" + ExtensionPoint.class.getSimpleName() + ")result, deserializationContext);");
		sourceWriter.outdent();
		sourceWriter.println("} else {");
		sourceWriter.indent();
		sourceWriter.println("return result;");
		sourceWriter.outdent();
		sourceWriter.println("}");
		sourceWriter.outdent();
		sourceWriter.println("}");

		for (JClassType JSONclassType : types) {
			generateInstantiate(sourceWriter, JSONclassType);
		}

		sourceWriter.println("return super.fromJson(jsonValue, clazz, deserializationContext);");

		sourceWriter.outdent();
		sourceWriter.println("}");

		sourceWriter.println("public " + Object.class.getSimpleName() + " fromJson(" + JSONValue.class.getSimpleName()
				+ " jsonValue, " + Object.class.getSimpleName()
				+ " instance, DeserializationContext deserializationContext) {");
		sourceWriter.indent();

		sourceWriter.println("if (jsonValue == null) {");
		sourceWriter.indent();
		sourceWriter.println("return null;");
		sourceWriter.outdent();
		sourceWriter.println("}");

		for (JClassType JSONclassType : types) {
			generateDeserialize(sourceWriter, JSONclassType);
		}

		sourceWriter.println("return super.fromJson(jsonValue, instance.getClass(), deserializationContext);");

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

	protected void generateExtendablePostProcessing(SourceWriter sourceWriter) {
		sourceWriter.println("fromJson(");
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

	protected String[] getFieldName(JField field) {
		Field fieldAnnotation = field.getAnnotation(Field.class);
		if (fieldAnnotation != null) {
			return new String[] {getFieldName(fieldAnnotation, field)};
		}
		ComplexField complexFieldAnnotation = field.getAnnotation(ComplexField.class);
		if (complexFieldAnnotation == null) {
			return new String[0];
		}
		
		List<String> result = new ArrayList<String>();
		
		for (Field subField : complexFieldAnnotation.value()) {
			result.add(getFieldName(subField, field));
		}
		
		return result.toArray(new String[0]);
	}

	protected String getFieldName(Field fieldAnnotation, JField field) {

		String fieldName = fieldAnnotation.value();

		if (fieldName == null || fieldName.length() == 0) {
			fieldName = field.getName();
		}

		if (fieldAnnotation.group() != null && fieldAnnotation.group().length() > 0) {
			fieldName = fieldAnnotation.group() + "$" + fieldName;
		}
		
		return fieldName;
	}
	
	protected boolean supportsField(JField field) {
		
		Field fieldAnnotation = field.getAnnotation(Field.class);

		ComplexField complexFieldAnnotation = field.getAnnotation(ComplexField.class);

		return (fieldAnnotation != null || complexFieldAnnotation != null);
	}
	
	private String generateSubFieldsAccess(String[] fields, String jsonObject) {
		String accessor = "get(" + jsonObject + ", new String[] {";
		
		for (int i = 0; i < fields.length; i++) {
			if (i > 0) {
				accessor += ",";
			}
			accessor += "\"" + fields[i] + "\""; 
		}
		
		return accessor + "})";
	}
	
	protected void generateField(SourceWriter sourceWriter, JClassType toType, JField field)
			throws UnableToCompleteException {

		if (!supportsField(field)) {
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

		String[] fieldNames = getFieldName(field);

		if (fd.type.isArray() != null) {
			//TODO - what about arrays?
		} else if (fd.type.isClassOrInterface() != null
				&& fd.type.isClassOrInterface().isAssignableTo(typeOracle.findType(Collection.class.getName()))) {

			sourceWriter.println(JSONValue.class.getSimpleName() + " jsonValue" + field.getName()
					+ " = " + generateSubFieldsAccess(fieldNames, "jsonObject") + ";");
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
				sourceWriter.println(Class.class.getSimpleName() + "<?> targetClazz = "
						+ targetType.getQualifiedSourceName() + ".class;");
			} else {
				sourceWriter.println(Class.class.getSimpleName() + "<?> targetClazz = jsonizerContext.getPropertyType("
						+ toType.getQualifiedSourceName() + ".class, \"" + fieldNames + "\");");
			}

			sourceWriter.println("if (targetClazz != null) {");
			sourceWriter.indent();

			sourceWriter.println(JSONArray.class.getSimpleName() + " jsonArray = jsonValue" + field.getName()
					+ ".isArray();");
			sourceWriter.println("if (jsonArray != null) {");
			sourceWriter.indent();

			JClassType classType = (JClassType) fd.type;
			
			sourceWriter.println(classType.getQualifiedSourceName() + " _jsonCollection = (" + classType.getQualifiedSourceName() + ")createInstance(" + classType.getQualifiedSourceName() + ".class);");
			sourceWriter.println("if (_jsonCollection == null) {");
			sourceWriter.indent();
			if (!classType.isDefaultInstantiable()) {
				sourceWriter.println("throw new " + RuntimeException.class.getCanonicalName() + "(\"Unable to create instance of " + classType.getQualifiedSourceName() + " class.\");"); 	
			} else {
				sourceWriter.println("_jsonCollection = new " +  classType.getQualifiedSourceName() + "();");
			}
			sourceWriter.println("");
			sourceWriter.outdent();
			sourceWriter.println("}");
			generateSetter(sourceWriter, fd, field, "(" + classType.getQualifiedSourceName()
					+ ")fromJson(jsonArray, targetClazz, _jsonCollection, deserializationContext)");
			sourceWriter.outdent();
			sourceWriter.println("} else {");
			sourceWriter.indent();
			generateSetter(sourceWriter, fd, field, "(" + classType.getQualifiedSourceName() + ")fromJson(jsonValue"
					+ field.getName() + ", targetClazz, deserializationContext)");
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
			String nonPrimitiveClassName = field.getType().getQualifiedSourceName();
			
			if (field.getType().isPrimitive() != null) {
				nonPrimitiveClassName = field.getType().isPrimitive().getQualifiedBoxedSourceName();
			}

			sourceWriter.println("_jsonResult = fromJson(" + generateSubFieldsAccess(fieldNames, "jsonObject") + ", " + nonPrimitiveClassName
					+ ".class, deserializationContext);");

			if (field.getType().isPrimitive() != null) {
				sourceWriter.println("if (_jsonResult != null) {");
				sourceWriter.indent();
				generateSetter(sourceWriter, fd, field, "(" + nonPrimitiveClassName + ")_jsonResult");
				sourceWriter.println("}");
				sourceWriter.outdent();
			} else {
				generateSetter(sourceWriter, fd, field, "(" + nonPrimitiveClassName + ")_jsonResult");
			}
		}
	}

	// TODO, reuse existing instances
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

	protected void generatePointNameResolver(SourceWriter sourceWriter, JClassType classType)
		throws UnableToCompleteException {

		JClassType toType;

		try {
			toType = RebindUtils.getGenericsFromInterfaceType(classType, typeOracle.findType(IJsonObject.class
					.getName()), 0);
		} catch (NotFoundException e) {
			logger.log(Type.ERROR, "Unable to extract generic type class from interface");
			throw new UnableToCompleteException();
		}

		JsonObject jsonAnnotation = toType.getAnnotation(JsonObject.class);
		if (jsonAnnotation == null) {
			return;
		}
		
		String value = jsonAnnotation.value();
		String group = jsonAnnotation.group();
		
		if (value == null || value.length() == 0) {
			return;
		}
		
		String result = value;
		
		if (group != null && group.length() > 0) {
			result = group + "$" + result;
		}
		
		sourceWriter.println("if (type.getName().equals("
				+ toType.getQualifiedSourceName() + ".class.getName())) {");
		sourceWriter.indent();
		sourceWriter.println("return \"" + result + "\";");
		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	protected void generateInstantiate(SourceWriter sourceWriter, JClassType classType)
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
		sourceWriter
				.println(toType.getQualifiedSourceName() + " data = new " + toType.getQualifiedSourceName() + "();");
		sourceWriter.println("return fromJson(jsonValue, data, deserializationContext);");
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

		sourceWriter.println("if (jsonValue.isObject() != null && instance.getClass().getName().equals("
				+ toType.getQualifiedSourceName() + ".class.getName())) {");
		sourceWriter.indent();

		// Using deserializator
		sourceWriter.println(JsonDeserializer.class.getSimpleName() + "<" + toType.getQualifiedSourceName() + ", "
				+ JSONValue.class.getSimpleName() + "> deserializer = jsonizerContext." + "getDeserializer("
				+ toType.getQualifiedSourceName() + ".class);");

		sourceWriter.println("if (deserializer != null) {");
		sourceWriter.indent();
		sourceWriter.println("Object result = deserializer.deserialize(jsonValue, deserializationContext);");
		sourceWriter.println("");
		
		try {
			if (toType.isAssignableTo(typeOracle.getType(ExtensionPoint.class.getName()))) {
				sourceWriter.println("if (result != null && jsonValue != null && jsonValue.isObject() != null) {");
				sourceWriter.indent();
				sourceWriter.println("return fromJson(jsonValue.isObject(), " + toType.getQualifiedSourceName() + ".class, (" + toType.getQualifiedSourceName() + ")result, deserializationContext);");
				sourceWriter.outdent();
				sourceWriter.println("} else {");
				sourceWriter.indent();
				sourceWriter.println("return result;");
				sourceWriter.outdent();
				sourceWriter.println("}");
			} else {
				sourceWriter.println("return result;");
			}
		} catch (NotFoundException e) {
			sourceWriter.println("return result;");
		}
		sourceWriter.outdent();
		sourceWriter.println("}");

		sourceWriter.println("");
		sourceWriter.println(JSONObject.class.getSimpleName() + " jsonObject = jsonValue.isObject();");
		sourceWriter.println("Object _jsonResult;");

		sourceWriter.println(toType.getQualifiedSourceName() + " data = (" + toType.getQualifiedSourceName()
				+ ")instance;");

		for (JField field : toType.getFields()) {
			generateField(sourceWriter, toType, field);
		}

		JClassType returnType = toType;
		
		toType = toType.getSuperclass();

		while (toType != null) {
			for (JField field : toType.getFields()) {
				generateField(sourceWriter, toType, field);
			}

			toType = toType.getSuperclass();
		}

		try {
			if (returnType.isAssignableTo(typeOracle.getType(ExtensionPoint.class.getName()))) {
				sourceWriter.println("if (data instanceof " + ExtensionPoint.class.getSimpleName() + ") {");
				sourceWriter.indent();
				sourceWriter.println("return fromJson(jsonValue.isObject(), " + returnType.getQualifiedSourceName() + ".class, data, deserializationContext);");
				sourceWriter.outdent();
				sourceWriter.println("} else {");
				sourceWriter.indent();
				sourceWriter.println("return data;");
				sourceWriter.outdent();
				sourceWriter.println("}");
			} else {
				sourceWriter.println("return data;");
			}
		} catch (NotFoundException e) {
			sourceWriter.println("return data;");
		}
		sourceWriter.outdent();
		sourceWriter.println("}");
	}

	protected String[] getImports() {
		return new String[] { GWT.class.getCanonicalName(), JSONArray.class.getCanonicalName(),
				JSONValue.class.getCanonicalName(), JSONObject.class.getCanonicalName(),
				JsonDeserializer.class.getCanonicalName(), Map.class.getCanonicalName(),
				DeserializationContext.class.getCanonicalName(), HashMap.class.getCanonicalName(),
				ExtensionPoint.class.getCanonicalName()};
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

		composerFactory.setSuperclass(ExtendableJsonizer.class.getCanonicalName());

		return composerFactory.createSourceWriter(context, printWriter);
	}
}