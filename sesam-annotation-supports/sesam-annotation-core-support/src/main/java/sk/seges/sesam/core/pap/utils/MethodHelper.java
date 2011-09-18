package sk.seges.sesam.core.pap.utils;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.builder.NameTypeUtils;
import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class MethodHelper {

	protected ProcessingEnvironment processingEnv;
	private NameTypeUtils nameTypes;

	public static final String SETTER_PREFIX = "set";
	public static final String GETTER_PREFIX = "get";
	public static final String GETTER_IS_PREFIX = "is";

	public MethodHelper(ProcessingEnvironment processingEnv, NameTypeUtils nameTypes) {
		this.processingEnv = processingEnv;
		this.nameTypes = nameTypes;
	}

	public String toMethod(String name) {

		if (name.length() < 2) {
			return name.toUpperCase();
		}

		return name.substring(0, 1).toUpperCase() + name.substring(1);
	}

	public String toMethod(String prefix, String fieldName) {
		PathResolver pathResolver = new PathResolver(fieldName);

		if (pathResolver.isNested()) {

			int i = 0;

			String result = "";

			while (pathResolver.hasNext()) {
				if (i > 0) {
					result += ".";
				}
				String path = pathResolver.next();

				if (pathResolver.hasNext()) {
					result += toGetter(path);
				} else {
					result += toMethod(prefix, path);
				}
				i++;
			}

			return result;
		}

		return prefix + toMethod(fieldName);
	}

	public String toSetter(ExecutableElement method) {
		if (method.getSimpleName().toString().startsWith(GETTER_PREFIX)) {
			return toSetter(method.getSimpleName().toString().substring(GETTER_PREFIX.length()));
		}
		return toSetter(method.getSimpleName().toString());
	}

	public String toField(String fieldName) {
		String[] pathParts = fieldName.split("\\.");
		String result = "";

		for (String path : pathParts) {
			result += toMethod(path);
		}

		if (result.length() < 2) {
			return result.toLowerCase();
		}

		return result.substring(0, 1).toLowerCase() + result.substring(1);
	}

	public String toGetter(String fieldName) {
		return toMethod(GETTER_PREFIX, fieldName) + "()";
	}

	public String toGetter(ExecutableElement method) {
		return toGetter(method.getSimpleName().toString());
	}

	public String toSetter(String fieldName) {
		return toMethod(SETTER_PREFIX, fieldName);
	}

	public String toField(ExecutableElement getterMethod) {

		String result = "";

		if (getterMethod.getSimpleName().toString().startsWith(GETTER_PREFIX)) {
			result = getterMethod.getSimpleName().toString().substring(GETTER_PREFIX.length());
		} else {
			result = getterMethod.getSimpleName().toString();
		}

		if (result.length() < 2) {
			return result.toLowerCase();
		}

		return result.substring(0, 1).toLowerCase() + result.substring(1);
	}

	public Element getField(ExecutableElement method) {
		return getField(method.getEnclosingElement(), toField(method));
	}
	
	public VariableElement getField(Element element, String name) {
		List<VariableElement> fields = ElementFilter.fieldsIn(element.getEnclosedElements());
		
		for (VariableElement field: fields) {
			if (field.getSimpleName().toString().equals(name)) {
				return field;
			}
		}
		
		return null;
	}
	
	public void copyMethodDefinition(ExecutableElement method, FormattedPrintWriter pw) {
		for (Modifier modifier: method.getModifiers()) {
			if (!modifier.equals(Modifier.ABSTRACT)) {
				pw.print(modifier.toString() + " ");
			}
		}
		pw.print(method.getReturnType(), " " + method.getSimpleName().toString() + "(");
		
		int i = 0;
		for (VariableElement parameter: method.getParameters()) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print(nameTypes.toType(parameter.asType()), " " + parameter.getSimpleName().toString());
			i++;
		}
		
		pw.print(")");
	}
	
	public void copyAnnotations(Element element, FormattedPrintWriter pw) {
		for (AnnotationMirror annotation: element.getAnnotationMirrors()) {
			pw.print("@", nameTypes.toType(annotation.getAnnotationType()));
			
			if (annotation.getElementValues().size() > 0) {
				pw.print("(");
				int i = 0;
				for (Entry<? extends ExecutableElement, ? extends AnnotationValue> annotationValue: annotation.getElementValues().entrySet()) {
					if (i > 0) {
						pw.print(", ");
						pw.println("		");
					}
					pw.print( annotationValue.getKey().getSimpleName() + " = " + annotationValueToString(annotationValue.getValue()));
					i++;
				}
				pw.print(")");
			}
			pw.println();
		}
	}

	private static boolean isArray(final Object obj) {
		if (obj != null)
			return obj.getClass().isArray();
		return false;
	}
	 
	private String annotationValueToString(Object value) {
		if (isArray(value)) {
			String result = "{";
			int i = 0;
			for (Object obj: (Object[])value) {
				if (i > 0) {
					result += ", ";
				}
				result += annotationValueToString(obj);
				i++;
			}
			return result + "}";
		}
		return value.toString();
	}
	
	interface ConstructorPrinter {

		boolean existsParameter(String field, ExecutableElement constructor);
		boolean existsField(String field, ExecutableElement constructor);

		void construct(TypeMirror type, PrintWriter pw);
		void construct(NamedType type, PrintWriter pw);

		void finish(List<String> initializedFields, ExecutableElement superConstructor, PrintWriter pw);
	}

	class DefaultConstructorPrinter implements ConstructorPrinter {

		@Override
		public boolean existsParameter(String field, ExecutableElement constructor) {
			for (VariableElement parameter: constructor.getParameters()) {
				if (parameter.getSimpleName().toString().equals(field)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public void construct(TypeMirror type, PrintWriter pw) {
			pw.print("new " + type.toString() + "()");
		}

		@Override
		public void construct(NamedType type, PrintWriter pw) {
			pw.print("new " + type.toString(ClassSerializer.CANONICAL, true) + "()");
		}

		@Override
		public void finish(List<String> initializedFields, ExecutableElement superConstructor, PrintWriter pw) {
			for (VariableElement parameter: superConstructor.getParameters()) {
				String parameterName = parameter.getSimpleName().toString();
				
				if (!initializedFields.contains(parameterName)) {
					pw.print("this." + parameterName + " = ");
					construct(parameter.asType(), pw);
					pw.println(";");
				}
			}

			for (VariableElement parameter: superConstructor.getParameters()) {
				String parameterName = parameter.getSimpleName().toString();
				if (!initializedFields.contains(parameterName)) {
					pw.print("this." + parameterName + " = ");
					construct(parameter.asType(), pw);
					pw.println(";");
				}
			}
		}

		@Override
		public boolean existsField(String field, ExecutableElement constructor) {
			List<VariableElement> fields = ElementFilter.fieldsIn(constructor.getEnclosingElement().getEnclosedElements());
			
			for (VariableElement classField: fields) {
				if (classField.getSimpleName().equals(field)) {
					return true;
				}
			}
			
			return false;
		}
	}

	public void copyConstructors(NamedType outputName, TypeElement fromType, final ExecutableElement superTypeConstructorElement, PrintWriter pw, ParameterElement... additionalParameters) {
		copyConstructors(outputName, fromType, superTypeConstructorElement, pw, true, additionalParameters);
	}
	
	public void copyConstructors(NamedType outputName, TypeElement fromType, final ExecutableElement superTypeConstructorElement, PrintWriter pw, boolean callSuperConstructor, ParameterElement... additionalParameters) {
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(fromType.getEnclosedElements());
		
		for (ExecutableElement constructor: constructors) {
			copyConstructor(outputName, constructor, superTypeConstructorElement, pw, new DefaultConstructorPrinter(), callSuperConstructor, additionalParameters);
		}
	}
	
	public void copyConstructors(NamedType outputName, TypeElement fromType, PrintWriter pw, ParameterElement... additionalParameters) {
		List<ExecutableElement> constructors = ElementFilter.constructorsIn(fromType.getEnclosedElements());
		
		for (ExecutableElement constructor: constructors) {
			copyConstructor(outputName, constructor, pw, additionalParameters);
		}
	}
	
	public void copyConstructor(NamedType outputName, ExecutableElement constructor, PrintWriter pw, ParameterElement... additionalParameters) {
		copyConstructor(outputName, constructor, constructor, pw, new DefaultConstructorPrinter(), true, additionalParameters);
	}
		
	private void copyConstructor(NamedType outputName, ExecutableElement constructor, ExecutableElement superConstructor, PrintWriter pw, ConstructorPrinter constructorPrinter, boolean callSuperConstructor, ParameterElement... additionalParameters) {

		List<String> initializedFields = new ArrayList<String>();
		
		pw.print("public " + outputName.getSimpleName() + "(");
		int i = 0;
		for (VariableElement parameter: constructor.getParameters()) {
			if (i > 0) {
				pw.print(", ");
			}
			
			String parameterName = parameter.getSimpleName().toString();
			
			if (parameter.asType().getKind().equals(TypeKind.DECLARED)) {
				pw.print(((DeclaredType)parameter.asType()).asElement().getSimpleName().toString() + " " + parameterName);
			} else {
				pw.print(parameter.asType().toString() + " " + parameterName);
			}
			
			i++;
		}
		
		for (ParameterElement additionalParameter: additionalParameters) {
			if (i > 0) {
				pw.print(", ");
			}
			
			String parameterName = additionalParameter.getName().toString();
			
//			if (additionalParameter.asType().getKind().equals(TypeKind.DECLARED)) {
//				String simpleTypeName =  .asElement().getSimpleName().toString();
//				pw.print(simpleTypeName + " " + parameterName);
//			} else {
			
				pw.print(additionalParameter.getType().toString(ClassSerializer.SIMPLE, true) + " " + parameterName);
//			}

			i++;
		}
		
		pw.println(") {");
		if (callSuperConstructor) {
			pw.print("super(");
		} else {
			pw.print("this(");
		}
		i = 0;
		for (VariableElement parameter: superConstructor.getParameters()) {
			if (i > 0) {
				pw.print(", ");
			}
			
			String parameterName = parameter.getSimpleName().toString();
			if (constructorPrinter.existsParameter(parameterName, constructor)) {
				pw.print(parameterName);
			} else {
				constructorPrinter.construct(parameter.asType(), pw);
			}
			initializedFields.add(parameterName);
			i++;
		}
		
		if (callSuperConstructor) {
			pw.println(");");
			for (VariableElement parameter: superConstructor.getParameters()) {
				String name = parameter.getSimpleName().toString();
				if (!constructorPrinter.existsField(name, superConstructor)) {
					pw.println("this." + name + " = " + name + ";");
					initializedFields.add(name);
				}
			}
			
			for (ParameterElement additionalParameter: additionalParameters) {
				String name = additionalParameter.getName();
				if (!constructorPrinter.existsField(name, superConstructor)) {
					pw.println("this." + name + " = " + name + ";");
					initializedFields.add(name);
				}
			}

			constructorPrinter.finish(initializedFields, superConstructor, pw);
		} else {
			for (ParameterElement additionalParameter: additionalParameters) {
				if (i > 0) {
					pw.print(", ");
				}
				pw.print(additionalParameter.getName().toString());
				i++;
			}
			pw.println(");");
		}
		
		pw.println("}");
		pw.println();
	}
}