package sk.seges.sesam.core.pap.model;

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

import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;

public class PojoElement {

	private final TypeElement element;
	private final ProcessingEnvironment processingEnv;
	
	enum MethodResolver {
		SETTER {
			@Override
			public String toMethod(ExecutableElement method) {
				return MethodHelper.toSetter(method);
			}

			@Override
			public boolean equalsMethodParameters(ExecutableElement elementMethod, ExecutableElement method, TypeElement owner, ProcessingEnvironment processingEnv) {
				return equalsParameters(elementMethod, method, owner, processingEnv);
			}

		}, GETTER {
			@Override
			public String toMethod(ExecutableElement method) {
				String getter = MethodHelper.toGetter(method);
				return getter.substring(0, getter.length() - 2);
			}

			@Override
			public boolean equalsMethodParameters(ExecutableElement elementMethod, ExecutableElement method, TypeElement owner, ProcessingEnvironment processingEnv) {
				return equalsParameters(method, elementMethod, owner, processingEnv);
			}

		}, ISGETTER {
			@Override
			public String toMethod(ExecutableElement method) {
				String getter = MethodHelper.toIsGetter(method);
				return getter.substring(0, getter.length() - 2);
			}

			@Override
			public boolean equalsMethodParameters(ExecutableElement elementMethod, ExecutableElement method, TypeElement owner, ProcessingEnvironment processingEnv) {
				return equalsParameters(method, elementMethod, owner, processingEnv);
			}
		};
		
		public abstract String toMethod(ExecutableElement method);
		public abstract boolean equalsMethodParameters(ExecutableElement elementMethod, ExecutableElement method, TypeElement owner, ProcessingEnvironment processingEnv);
		
		private static boolean equalsParameters(ExecutableElement setterMethod, ExecutableElement getterMethod, TypeElement owner, ProcessingEnvironment processingEnv) {
			
			if (setterMethod.getParameters().size() == 0) {
				return false;
			}
			
			return isAssignable(
					getErasuredType(getterMethod.getReturnType(), owner), 
					getErasuredType(setterMethod.getParameters().get(0).asType(), owner), processingEnv);
		}
		
		private static TypeMirror getErasuredType(TypeMirror typeMirror, TypeElement owner) {
			if (typeMirror.equals(TypeKind.TYPEVAR)) {
				TypeMirror ensuredType = ProcessorUtils.erasure(owner, (TypeVariable) typeMirror);
				if (ensuredType != null) {
					return ensuredType;
				}
			}
			
			return typeMirror;
		}
	}
	
	public PojoElement(TypeElement element, ProcessingEnvironment processingEnv) {
		this.element = element;
		this.processingEnv = processingEnv;
	}
	
	static boolean isAssignable(TypeMirror type1, TypeMirror type2, ProcessingEnvironment processingEnv) {
		if (type1.getKind().equals(TypeKind.TYPEVAR) && type2.getKind().equals(TypeKind.TYPEVAR)) {
			return type1.toString().equals(type2.toString());
		}
		
		return processingEnv.getTypeUtils().isAssignable(type1, type2);
	}

	public boolean hasSetterMethod(ExecutableElement method) {
		return getMethod(element, element, method, MethodResolver.SETTER) != null;
	}

	public boolean hasGetterMethod(ExecutableElement method) {
		return getMethod(element, element, method, MethodResolver.GETTER) != null &&
		getMethod(element, element, method, MethodResolver.ISGETTER) != null;
	}

	public ExecutableElement getSetterMethod(ExecutableElement method) {
		return getMethod(element, element, method, MethodResolver.SETTER);
	}

	public ExecutableElement getGetterMethod(ExecutableElement method) {
		ExecutableElement result = getMethod(element, element, method, MethodResolver.GETTER);
		
		if (result != null) {
			return result;
		}
		return getMethod(element, element, method, MethodResolver.ISGETTER);
	}

	private ExecutableElement getMethod(TypeElement owner, TypeElement element, ExecutableElement method, MethodResolver methodResolver) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());

		for (ExecutableElement elementMethod : methods) {

			if (elementMethod.getModifiers().contains(Modifier.PUBLIC) && 
				elementMethod.getSimpleName().toString().equals(methodResolver.toMethod(method))) {
				
				if (methodResolver.equalsMethodParameters(elementMethod, method, owner, processingEnv)) {
					return elementMethod;
				}
			}
		}
		
		if (element.getSuperclass() != null && element.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
			ExecutableElement result = getMethod(owner, (TypeElement) ((DeclaredType) element.getSuperclass()).asElement(), method, methodResolver);
			
			if (result != null) {
				return result;
			}
		}

		for (TypeMirror typeInterface: element.getInterfaces()) {
			ExecutableElement result = getMethod(owner, (TypeElement)((DeclaredType)typeInterface).asElement(), method, methodResolver);
			
			if (result != null) {
				return result;
			}
		}
		
		return null;
	}
}