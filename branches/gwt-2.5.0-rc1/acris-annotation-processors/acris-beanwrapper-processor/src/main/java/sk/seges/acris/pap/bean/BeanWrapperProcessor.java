package sk.seges.acris.pap.bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.acris.core.client.annotation.BeanWrapper;
import sk.seges.acris.pap.bean.configurer.BeanWrapperProcessorConfigurer;
import sk.seges.acris.pap.bean.model.BeanWrapperImplementationType;
import sk.seges.acris.pap.bean.model.BeanWrapperType;
import sk.seges.sesam.core.pap.comparator.ExecutableComparator;
import sk.seges.sesam.core.pap.configuration.api.ProcessorConfigurer;
import sk.seges.sesam.core.pap.model.PojoElement;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.printer.MethodPrinter;
import sk.seges.sesam.core.pap.processor.MutableAnnotationProcessor;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

import com.google.gwt.core.client.GWT;

@SupportedSourceVersion(SourceVersion.RELEASE_6)
public class BeanWrapperProcessor extends MutableAnnotationProcessor {

	@Override
	protected MutableDeclaredType[] getOutputClasses(RoundContext context) {
		return new MutableDeclaredType[] {
			new BeanWrapperImplementationType(context.getMutableType(), processingEnv)
		};
	}
	
	@Override
	protected ProcessorConfigurer getConfigurer() {
		return new BeanWrapperProcessorConfigurer();
	}	
	
	@Override
	protected void processElement(ProcessorContext context) {
		
		List<ExecutableElement> processedMethods = new ArrayList<ExecutableElement>();
		
		MutableDeclaredType processingType = (MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(context.getTypeElement().asType());
		
		while (processingType != null) {

			List<ExecutableElement> methods = ElementFilter.methodsIn(processingType.asElement().getEnclosedElements());
			
			Collections.sort(methods, new ExecutableComparator());

			for (ExecutableElement method: methods) {
	
				if (!MethodHelper.isGetterMethod(method) && !MethodHelper.isSetterMethod(method)) {
					continue;
				}
	
				if (method.getModifiers().contains(Modifier.PRIVATE) || method.getModifiers().contains(Modifier.PROTECTED)) {
					continue;
				}
	
				boolean found = false;
				for (ExecutableElement processedMethod: processedMethods) {
					if (new ExecutableComparator(false).compare(processedMethod, method) == 0) {
						found = true;
						break;
					}
				}
				
				if (found) {
					continue;
				}
				processedMethods.add(method);
			}
			
			processingType = processingType.getSuperClass();
			
			if (processingType != null && processingType.toString(ClassSerializer.CANONICAL, false).equals(Object.class.getCanonicalName())) {
				processingType = null;
			}
		}
		
		processMethods(context, processedMethods);
	}
	
	protected static final String BEAN_WRAPPER_CONTENT = "beanWrapperContent";

	protected void processMethods(ProcessorContext context, List<ExecutableElement> methods) {
		
		FormattedPrintWriter pw = context.getPrintWriter();
		
		pw.println("protected ", erasure(context.getTypeElement().asType(), context.getTypeElement()), " " + BEAN_WRAPPER_CONTENT + ";");
		pw.println();

		PojoElement pojoElement = new PojoElement(context.getTypeElement(), processingEnv);
		
		List<String> wrappedFields = new ArrayList<String>();
		
		for (ExecutableElement method: methods) {
			if (MethodHelper.isSetterMethod(method)) {
				
				//Only 1 parameter in setter method is supported
				if (method.getParameters().size() != 1) {
					continue;
				}
				
				ExecutableElement getterMethod = pojoElement.getGetterMethod(method);
				
				if (getterMethod == null) {
					continue;
				}
				
				VariableElement parameter = method.getParameters().get(0);
				
				if (isBean(parameter.asType())) {
					generateSetterForBean(pw, method, parameter, getterMethod, context.getOutputType(), context.getTypeElement());
				} else {
					generateSetterForPrimitive(pw, method, parameter, getterMethod, context.getTypeElement());
				}
			} else {
				
				if (isBean(method.getReturnType())) {
					generateGetterForBean(pw, method, context.getOutputType(), wrappedFields, context.getTypeElement());
				} else {
					generateGetterForPrimitive(pw, method, context.getTypeElement());
				}
			}
		}
		
		generateWrapperMethods(pw, context.getMutableType());

		generateGetBeanAttributes(pw, methods);
		generateSetBeanAttributes(pw, methods, context.getMutableType(), context.getTypeElement());

		generateClearWrappersMethod(pw, wrappedFields);
	}
	
	private void generateClearWrappersMethod(FormattedPrintWriter pw, List<String> wrappedFields) {
		pw.println("private void clearWrappers() {");
		
		for (String field : wrappedFields) {
			pw.println(field + " = null;");
		}
		
		pw.println("}");
	}

	private final static String ATTRIBUTE_NAME = "attr";
	
	protected void castFromString(FormattedPrintWriter pw, TypeMirror type, String value, TypeElement owner) {
		if (type.getKind().isPrimitive()) {
			pw.print("(", processingEnv.getTypeUtils().boxedClass((PrimitiveType) type), ")" + value);
		} else {
			pw.print("(", erasure(type, owner), ")" + value);
		}
	}

	private MutableTypeMirror erasure(TypeMirror type, TypeElement owner) {
		switch (type.getKind()) {
		case TYPEVAR:
			TypeMirror erasure = ProcessorUtils.erasure(owner, (TypeVariable) type);
			if (erasure == null) {
				return processingEnv.getTypeUtils().getTypeVariable(((TypeVariable) type).asElement().getSimpleName().toString());
			}
			return processingEnv.getTypeUtils().toMutableType(erasure);
		case DECLARED:
			DeclaredType declaredType = (DeclaredType)type;
			return processingEnv.getTypeUtils().toMutableType(declaredType).stripTypeParametersTypes();
		default:
			return processingEnv.getTypeUtils().toMutableType(type);
		}
	}
	
	protected void generateSetBeanAttributes(FormattedPrintWriter pw, List<ExecutableElement> methods, MutableDeclaredType beanType, TypeElement owner) {
		// create the set attribute method
		pw.println("public void setBeanAttribute(", String.class, " " + ATTRIBUTE_NAME + ", ", Object.class, " value) {");

		for (ExecutableElement method: methods) {
			if (MethodHelper.isSetterMethod(method) && method.getParameters().size() == 1) {
				pw.println("if (" + ATTRIBUTE_NAME + ".equals(\"" + MethodHelper.toField(method) + "\")) { ");
				pw.print("this." + method.getSimpleName() + "(");
				castFromString(pw, method.getParameters().get(0).asType(), "value", owner);
				pw.println(");");
				pw.print("} else ");
			}
		}
		
		pw.println("if (attr.equals(\"" + BEAN_WRAPPER_CONTENT + "\")) {");
		pw.println("this." + BEAN_WRAPPER_CONTENT + " = (", beanType.clone().stripTypeParametersTypes(), ") value;");
		pw.println("}");
		pw.println("}");
		pw.println();
	}

	protected void generateGetBeanAttributes(FormattedPrintWriter pw, List<ExecutableElement> methods) {
		// create the getAttribute method
		pw.println("public ", Object.class, " getBeanAttribute(", String.class, " attr) {");

		for (ExecutableElement method: methods) {
			if (MethodHelper.isGetterMethod(method) && method.getParameters().size() == 0) {
				pw.println("if (attr.equals(\"" + MethodHelper.toField(method) + "\")) {");
				pw.println("return " + castToString(method.getReturnType(), "this." + method.getSimpleName().toString() + "()") + ";");
				pw.print("} else ");
			}
		}
		
		pw.println("if (attr.equals(\"" + BEAN_WRAPPER_CONTENT + "\")) {");
		pw.println("return " + BEAN_WRAPPER_CONTENT + ";");
		pw.println("} else {");
		pw.println("return null;");
		pw.println("}");
		pw.println("}");
		pw.println();
	}

	protected String castToString(TypeMirror type, String value) {
		return value;
	}

	protected void generateWrapperMethods(FormattedPrintWriter pw, MutableDeclaredType beanType) {
		MutableDeclaredType beanElement = beanType.clone().stripTypeParametersTypes();

		pw.println("public void setBeanWrapperContent(", beanElement, " " + BEAN_WRAPPER_CONTENT + ") {");
		pw.println("this." + BEAN_WRAPPER_CONTENT + " = (", beanElement, ") " + BEAN_WRAPPER_CONTENT + ";");
		pw.println("clearWrappers();");
		pw.println("}");
		pw.println();
		pw.println("public ", beanElement, " getBeanWrapperContent() {");
		pw.println("return this." + BEAN_WRAPPER_CONTENT + ";");
		pw.println("}");
		pw.println();
	}

	protected void generateSetterForPrimitive(FormattedPrintWriter pw, ExecutableElement method, VariableElement parameter, ExecutableElement getter, TypeElement owner) {
		new MethodPrinter(pw, processingEnv).printMethodDefinition(method, owner);
		pw.println(" {");
		pw.println(BEAN_WRAPPER_CONTENT + "." + method.getSimpleName() + "(" + parameter.getSimpleName() + ");");
		pw.println("}");
		pw.println();
	}

	protected boolean isBean(TypeMirror type) {
		if (!type.getKind().equals(TypeKind.DECLARED)) {
			return false;
		}
		
		TypeMirror unboxType = unboxType(type);
		
		return (unboxType == null);
	}
	
	protected TypeMirror unboxType(TypeMirror type) {
		try {
			return processingEnv.getTypeUtils().unboxedType(type);
		} catch (Exception e) {
			return null;
		}
	}

	protected static final String NESTED_BEAN_WRAPPER = "__nested";

	protected void generateGetterForBean(FormattedPrintWriter pw, ExecutableElement method, MutableDeclaredType result, List<String> wrappedFields, TypeElement owner) {

		String field = MethodHelper.toField(method) + NESTED_BEAN_WRAPPER;
		TypeMirror returnType = method.getReturnType();

		MutableDeclaredType mutableReturnType = (MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(returnType);
		BeanWrapperImplementationType beanWrapperResultType = new BeanWrapperImplementationType(mutableReturnType, processingEnv);
		
		TypeElement beanWrapperResultTypeElement = processingEnv.getElementUtils().getTypeElement(beanWrapperResultType.getCanonicalName());
		
		if (beanWrapperResultTypeElement == null && mutableReturnType.getAnnotation(BeanWrapper.class) == null) {
			processingEnv.getMessager().printMessage(Kind.WARNING, "Falling back to non-beanwrapper primitive type - didn't find = " + beanWrapperResultType.getCanonicalName());
			generateGetterForPrimitive(pw, method, owner);
			return;
		}

		MutableDeclaredType wrapperType = new BeanWrapperType(mutableReturnType, processingEnv);

		pw.println("private ", wrapperType, " " + field + ";");
		pw.println();
		
		new MethodPrinter(pw, processingEnv).printMethodDefinition(method, owner);
		pw.println(" {");
		
		pw.println("if (" + BEAN_WRAPPER_CONTENT + "." + method.getSimpleName().toString() + "() == null) {");
		pw.println("return null;");
		pw.println("}");
		
		pw.println("if (this." + field + " == null) {");
		pw.println("this." + field + " = ", GWT.class, ".create(", 
				new BeanWrapperImplementationType((MutableDeclaredType) mutableReturnType, processingEnv).clone().setTypeVariables(), ".class);");
		pw.println("if(" + BEAN_WRAPPER_CONTENT + " != null) {");
		pw.println("this." + field + ".setBeanWrapperContent(" + BEAN_WRAPPER_CONTENT + "." + method.getSimpleName().toString() + "());");
		pw.println("}");
		pw.println("}");
		pw.println("return this." + field + ".getBeanWrapperContent();");
		pw.println("}");

		addWrappedField(field, wrappedFields);
	}

	protected void addWrappedField(String field, List<String> wrappedFields) {
		wrappedFields.add(field);
	}

	protected void generateSetterForBean(FormattedPrintWriter pw, ExecutableElement method, 
			VariableElement parameter, ExecutableElement getter, MutableDeclaredType result, TypeElement owner) {
		String field = MethodHelper.toField(method) + NESTED_BEAN_WRAPPER;

		BeanWrapperImplementationType beanWrapperResultType = new BeanWrapperImplementationType((MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(parameter.asType()), processingEnv);
		
		TypeElement beanWrapperResultTypeElement = processingEnv.getElementUtils().getTypeElement(beanWrapperResultType.getCanonicalName());
		
		if (beanWrapperResultTypeElement == null) {
			processingEnv.getMessager().printMessage(Kind.WARNING, "Falling back to non-beanwrapper primitive type - didn't find = " + beanWrapperResultType.getCanonicalName());
			generateSetterForPrimitive(pw, method, parameter, getter, owner);
			return;
		}
		
		TypeMirror fieldType;
		
		if(getter != null) {
			// use getter to align field type with return type of GWT.create
			fieldType = getter.getReturnType();
		} else {
			fieldType = parameter.asType();
		}

		new MethodPrinter(pw, processingEnv).printMethodDefinition(method, owner);
		pw.println(" {");
		pw.println("if (" + parameter.getSimpleName().toString() + " == null) {");
		pw.println("return;");
		pw.println("}");
//		pw.println(parameter.asType(), " oldValue = " + BEAN_WRAPPER_CONTENT + "." + getter.getSimpleName().toString() + "();");
		pw.println(BEAN_WRAPPER_CONTENT + "." + method.getSimpleName().toString() + "(" + parameter.getSimpleName().toString() + ");");

		pw.println("if (this." + field + " == null) {");
		pw.println("this." + field + " = ", GWT.class, ".create(", new BeanWrapperImplementationType(
				(MutableDeclaredType) processingEnv.getTypeUtils().toMutableType(fieldType), processingEnv).clone().setTypeVariables(), ".class);");
		pw.println("}");
		pw.println("this." + field + ".setBeanWrapperContent((", fieldType, ")" + parameter.getSimpleName().toString() + ");");
		pw.println("}");
		pw.println();
	}
	
	protected void generateGetterForPrimitive(FormattedPrintWriter pw, ExecutableElement method, TypeElement owner) {
		new MethodPrinter(pw, processingEnv).printMethodDefinition(method, owner);
		pw.println("{");
		pw.println("if (" + BEAN_WRAPPER_CONTENT + " != null) {");
		pw.print("return " + BEAN_WRAPPER_CONTENT + "." + method.getSimpleName().toString() + "(");
		int i = 0;
		for (VariableElement parameter: method.getParameters()) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print(parameter.getSimpleName().toString());
			i++;
		}
		pw.println(");");
		pw.println("}");
		pw.println("return " + defaultValue(method.getReturnType()) + ";");
		pw.println("}");
		pw.println();
	}

	protected String defaultValue(TypeMirror type) {
		switch (type.getKind()) {
		case INT:
			return "0";
		case BOOLEAN:
			return "false";
		case BYTE:
			return "Byte.valueOf(\"0\").byteValue()";
		case CHAR:
			return "''";
		case DOUBLE:
			return "0d";
		case FLOAT:
			return "0f";
		case LONG:
			return "0L";
		case SHORT:
			return "Short.valueOf(\"0\").shortValue()";
		case DECLARED:
			DeclaredType declaredType = (DeclaredType)type;
			if (declaredType.asElement().getSimpleName().toString().equals(String.class.getSimpleName())) {
				return null;
			}
		}

		return "null";
	}	
}