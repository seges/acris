package sk.seges.sesam.core.pap.utils;

import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;


public class ProcessorUtils {

	public static boolean isUnboxedType(TypeMirror typeMirror, ProcessingEnvironment processingEnv) {
		try {
			return (processingEnv.getTypeUtils().unboxedType(typeMirror) != null);
		} catch (IllegalArgumentException e) {
		}

		return false;
	}

	public static boolean implementsType(TypeMirror t1, TypeMirror t2) {
		if (t1 == null || !t1.getKind().equals(TypeKind.DECLARED) || !t2.getKind().equals(TypeKind.DECLARED)) {
			return false;
		}
		
		DeclaredType dt1 = (DeclaredType)t1;
		DeclaredType dt2 = (DeclaredType)t2;

		if (dt1.asElement().equals(dt2.asElement())) {
			return true;
		}
		
		for (TypeMirror interfaceType: ((TypeElement)dt1.asElement()).getInterfaces()) {
			
			if (interfaceType.getKind().equals(TypeKind.DECLARED)) {
				if (((DeclaredType)interfaceType).asElement().equals(dt2.asElement())) {
					return true;
				}
				
				if (implementsType(interfaceType, t2)) {
					return true;
				}
			}
			
		}

		TypeMirror superClassType = ((TypeElement)dt1.asElement()).getSuperclass();
		
		if (superClassType.getKind().equals(TypeKind.DECLARED)) {
			if (((DeclaredType)superClassType).asElement().equals(dt2.asElement())) {
				return true;
			}		
		}

		return implementsType(superClassType, t2);
	}

	public static TypeMirror erasure(TypeElement typeElement, TypeVariable typeVar) {
		return erasure(typeElement, typeVar.asElement().getSimpleName().toString());
	}
	
	public static TypeMirror erasure(TypeElement typeElement, String parameterName) {

		TypeMirror erasureSuperclass = erasureSuperclass(typeElement, parameterName, null);
		
		if (erasureSuperclass != null) {
			return erasureSuperclass;
		}
		
		return erasureInterfaces(typeElement, parameterName);
	}

	public static TypeMirror erasureInterfaces(TypeElement typeElement, String parameterName) {

		for (TypeMirror interfaceType: typeElement.getInterfaces()) {
			if (interfaceType.getKind().equals(TypeKind.DECLARED)) {
				
				TypeElement currentTypeElement = (TypeElement)((DeclaredType)interfaceType).asElement();
				
				for (TypeParameterElement parameter: currentTypeElement.getTypeParameters()) {
					int i = 0;
					if (parameter.getSimpleName().toString().equals(parameterName)) {
						return ((DeclaredType)interfaceType).getTypeArguments().get(i);
					}
					i++;
				}
				
				TypeMirror erasuredSuperclass = erasureSuperclass(currentTypeElement, parameterName, typeElement);
				
				if (erasuredSuperclass != null) {
					return erasuredSuperclass;
				}
			}
		}
		
		return null;
	}

	public static TypeMirror erasureSuperclass(TypeElement typeElement, String parameterName, TypeElement owner) {

		Element currentElement = typeElement;
		
		while (currentElement != null && currentElement.asType().getKind().equals(TypeKind.DECLARED)) {
			TypeElement currentTypeElement = (TypeElement)currentElement;
			for (TypeParameterElement parameter: currentTypeElement.getTypeParameters()) {
				int i = 0;
				if (parameter.getSimpleName().toString().equals(parameterName)) {
					if (owner != null) {
						DeclaredType dc = (DeclaredType)owner.getSuperclass();
						return dc.getTypeArguments().get(i);
					}
					i++;
				}
			}
			
			TypeMirror erasure = erasureInterfaces(currentTypeElement, parameterName);

			if (erasure != null) {
				return erasure;
			}
			
			owner = currentTypeElement;
			
			if (currentTypeElement.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
				currentElement = ((DeclaredType)currentTypeElement.getSuperclass()).asElement();
			} else {
				currentElement = null;
			}
		}
		
		return null;
	}
	
	public static ExecutableElement getMethodByReturnType(TypeElement typeElement, TypeElement returnType, Types types) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(typeElement.getEnclosedElements());
		
		for (ExecutableElement method : methods) {
			if (method.getReturnType() != null) {
				//
				if (method.getReturnType().getKind().equals(TypeKind.TYPEVAR)) {
					TypeVariable typeVariable = (TypeVariable)method.getReturnType();
					if (types.isAssignable(returnType.asType(), typeVariable.getUpperBound())) {
						return method;
					}
				} else {
					if (types.isAssignable(returnType.asType(), method.getReturnType())) {
						return method;
					}
				}
			}
		}
		
		return null;
	}
	
	public static ExecutableElement getMethodByParameterType(String name, Element classElement, int paramIndex, TypeMirror parameter, Types types) {
		
		assert name != null;
		assert classElement != null;
		assert parameter != null;
		assert paramIndex >= 0;

		List<ExecutableElement> methods = ElementFilter.methodsIn(classElement.getEnclosedElements());
		
		for (ExecutableElement method : methods) {
			if (!method.getSimpleName().toString().equals(name)) {
				continue;
			}
			if (method.getParameters().size() <= paramIndex) {
				continue;
			}
			
			VariableElement variableElement = method.getParameters().get(paramIndex);
			
			if (types.isAssignable(variableElement.asType(), parameter)) {
				return method;
			}
		}
		
		return null;
	}

	/**
	 * Method determines whether typeElement implements or extends type
	 */
	public static boolean isAssignableFrom(TypeElement typeElement, MutableDeclaredType type) {
		assert typeElement != null;
		assert type != null;
		
		boolean result;

		result = isOfType(typeElement, type);
		if (result == true) {
			return true;
		}

		List<? extends TypeMirror> interfaces2 = typeElement.getInterfaces();
		TypeMirror superclass = typeElement.getSuperclass();

		result = isAssignable(superclass, type);
		if (result == true) {
			return true;
		}

		for (TypeMirror mirror : interfaces2) {
			if (isAssignable(mirror, type)) {
				return true;
			}
		}
		return false;
	}

	public static boolean isAssignable(TypeMirror mirror, MutableDeclaredType type) {
		assert mirror != null;
		assert type != null;

		if (mirror instanceof DeclaredType) {
			DeclaredType dt = (DeclaredType) mirror;
			TypeElement dte = (TypeElement) dt.asElement();

			if (isOfType(dte, type)) {
				return true;
			} else {
				return isAssignableFrom(dte, type);
			}
		}
		return false;
	}

	public static boolean isOfType(TypeElement te, MutableDeclaredType type) {
		return te.getQualifiedName().getClass().toString().equals(type.getQualifiedName());
	}

	public static ExecutableElement getMethod(String name, Element element) {
		assert name != null;
		assert element != null;
		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());

		for (ExecutableElement method : methods) {
			if (method.getSimpleName().toString().equals(name)) {
				return method;
			}
		}

		TypeElement typeElement = (TypeElement)element;
		
		if (typeElement.getSuperclass() != null && typeElement.getSuperclass().getKind().equals(TypeKind.DECLARED)) {
			ExecutableElement method = getMethod(name, ((DeclaredType)typeElement.getSuperclass()).asElement());
			if (method != null) {
				return method;
			}
		}
		
		for (TypeMirror interfaceType: typeElement.getInterfaces()) {
			if (interfaceType.getKind().equals(TypeKind.DECLARED)) {
				ExecutableElement method = getMethod(name, ((DeclaredType)interfaceType).asElement());
				
				if (method != null) {
					return method;
				}
			}
		}

		return null;
	}
	
	public static boolean hasMethod(String name, Element element) {
		return getMethod(name, element) != null;
	}
	
	public static ExecutableElement getOverrider(TypeElement element, ExecutableElement method, ProcessingEnvironment processingEnv) {
		List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());

		for (ExecutableElement classMethod : methods) {
			if (processingEnv.getElementUtils().overrides(classMethod, method, element)) {
				return classMethod;
			}
		}
		
		return method;
	}
}