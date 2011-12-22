package sk.seges.sesam.pap.model.utils;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.NullCheck;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.pap.model.annotation.Field;

public class TransferObjectHelper {

	public static final String DTO_SUFFIX = "Dto";
	public static final String DEFAULT_SUFFIX = "Configuration";

	private ProcessingEnvironment processingEnv;

	public TransferObjectHelper(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}

	//TODO Move to the configuration
	public static String getFieldPath(ExecutableElement method) {
		if (method == null) {
			return null;
		}

		Field fieldAnnotation = method.getAnnotation(Field.class);

		String fieldPath = MethodHelper.toField(method);

		if (fieldAnnotation != null && NullCheck.checkNull(fieldAnnotation.value()) != null) {
			fieldPath = fieldAnnotation.value();
		}

		return fieldPath;
	}

	private boolean isAssignable(TypeMirror type1, TypeMirror type2) {
		if (type1.getKind().equals(TypeKind.TYPEVAR) && type2.getKind().equals(TypeKind.TYPEVAR)) {
			return type1.toString().equals(type2.toString());
		}
		
		return processingEnv.getTypeUtils().isAssignable(type1, type2);
	}

	public boolean hasSetterMethod(TypeElement element, ExecutableElement method) {
		return hasSetterMethod(element, element, method);
	}
	
	private boolean hasSetterMethod(TypeElement owner, TypeElement element, ExecutableElement method) {

		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());

		for (ExecutableElement elementMethod : methods) {

			if (elementMethod.getModifiers().contains(Modifier.PUBLIC) && 
				elementMethod.getSimpleName().toString().equals(MethodHelper.toSetter(method)) && elementMethod.getParameters().size() == 1) {
				TypeMirror asType = elementMethod.getParameters().get(0).asType();
				if (asType.getKind().equals(TypeKind.TYPEVAR)) {
					TypeMirror ensuredType = ProcessorUtils.erasure(owner, (TypeVariable) asType);
					if (ensuredType != null) {
						asType = ensuredType;
					}
				}
				
				if (isAssignable(method.getReturnType(), asType)) {
					return true;
				}
				
				return false;
			}
		}
		
		if (element.getSuperclass() != null && element.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
			if (hasSetterMethod(owner, (TypeElement) ((DeclaredType) element.getSuperclass()).asElement(), method)) {
				return true;
			}
		}

		for (TypeMirror typeInterface: element.getInterfaces()) {
			if (hasSetterMethod(owner, (TypeElement)((DeclaredType)typeInterface).asElement(), method)) {
				return true;
			}
		}
		
		return false;
	}
}