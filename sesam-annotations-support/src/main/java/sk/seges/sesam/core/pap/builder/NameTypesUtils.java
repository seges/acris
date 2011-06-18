package sk.seges.sesam.core.pap.builder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;

import sk.seges.sesam.core.pap.builder.api.NameTypes;
import sk.seges.sesam.core.pap.model.InputClass;
import sk.seges.sesam.core.pap.model.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;

public class NameTypesUtils implements NameTypes {

	private Elements elements;
	
	public NameTypesUtils(Elements elements) {
		this.elements = elements;
	}
	
	private MutableType handleGenerics(MutableType simpleType, TypeMirror type) {
		if (type.getKind().equals(TypeKind.DECLARED)) {
			DeclaredType declaredType = (DeclaredType)type;
			if (declaredType.getTypeArguments() != null && declaredType.getTypeArguments().size() > 0) {
				TypeParameter[] typeParameters = new TypeParameter[declaredType.getTypeArguments().size()];
				for (int i = 0; i < declaredType.getTypeArguments().size(); i++) {
					TypeMirror typeParameter = declaredType.getTypeArguments().get(i);

					List<Type> boundTypes = new ArrayList<Type>();
					
					if (typeParameter.getKind().equals(TypeKind.TYPEVAR)) {
						TypeVariable typeVariable = (TypeVariable)typeParameter;
						TypeMirror upperBound = typeVariable.getUpperBound();

						if (upperBound != null && upperBound.getKind().equals(TypeKind.DECLARED)) {
							TypeElement element = (TypeElement)((DeclaredType)upperBound).asElement();
							
							if (element.getSuperclass() != null && !element.getSuperclass().getKind().equals(TypeKind.NONE)) {
								DeclaredType superClassType = (DeclaredType)element.getSuperclass();
								if (superClassType != null && !superClassType.toString().equals(Object.class.getCanonicalName())) {
									boundTypes.add(toType(superClassType));
								}

								for (TypeMirror typeInterface: element.getInterfaces()) {
									if (typeInterface != null && !typeInterface.toString().equals(Object.class.getCanonicalName())) {
										boundTypes.add(toType(typeInterface));
									}
								}
							} else {
								boundTypes.add(toType(upperBound));
							}
							typeParameters[i] = TypeParameterBuilder.get(typeParameter.toString(), boundTypes.toArray(new Type[] {}));
						}
					} else {
						boundTypes.add(toType(typeParameter));
						typeParameters[i] = TypeParameterBuilder.get(null, boundTypes.toArray(new Type[] {}));
					}
				}
				return TypedClassBuilder.get(simpleType, typeParameters);
			}
		}
		
		return simpleType;
	}

	public MutableType toType(TypeMirror typeMirror) {
		if (typeMirror.getKind().equals(TypeKind.DECLARED)) {
			DeclaredType declaredType = (DeclaredType)typeMirror;
			
			if (declaredType.getEnclosingType() != null && declaredType.getEnclosingType().getKind().equals(TypeKind.DECLARED)) {
				MutableType enclosedElement = toType(declaredType.getEnclosingType());
				return handleGenerics(new InputClass(enclosedElement, declaredType.asElement().getSimpleName().toString()), declaredType);
			}
	
			PackageElement packageElement = elements.getPackageOf(declaredType.asElement());
			return handleGenerics(new InputClass(packageElement.getQualifiedName().toString(), declaredType.asElement().getSimpleName().toString()), declaredType);
		}

		throw new RuntimeException("Unsupported type " + typeMirror.getKind());
	}
	
	public MutableType toType(Element element) {
		return toType(element.asType());
//		if (element.getEnclosingElement() != null && isDeclaredType(element.getEnclosingElement())) {
//			MutableType enclosedElement = toType(element.getEnclosingElement());
//			return handleGenerics(new InputClass(enclosedElement, element.getSimpleName().toString()), element);
//		}
//
//		PackageElement packageElement = elements.getPackageOf(element);
//		return handleGenerics(new InputClass(packageElement.getQualifiedName().toString(), element.getSimpleName().toString()), element);
	}
	
	private static NamedType toType(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		if (clazz.getEnclosingClass() != null) {
			return new InputClass(toType(clazz.getEnclosingClass()), clazz.getSimpleName());
		}
		return new InputClass(clazz.getPackage().getName(), clazz.getSimpleName());
	}

	public NamedType toType(Type javaType) {
		
		if (javaType instanceof Class) {
			return toType((Class<?>)javaType);
		}
		
		if (javaType instanceof NamedType) {
			return (NamedType)javaType;
		}
		
		return null;
	}

	private TypeParameter toTypeParameter(String typeParameter) {
		int extendsIndex = typeParameter.indexOf("extends");
		
		if (extendsIndex != -1) {
			String parameterType = typeParameter.substring(extendsIndex + "extends".length()).trim();
			
			return TypeParameterBuilder.get(typeParameter.substring(0, extendsIndex).trim(), 
						parameterType.equals("THIS") ? NamedType.THIS : toType(parameterType));
		}

		NamedType parameterType = toType(typeParameter);

		if (parameterType == null) {
			if (typeParameter.equals("THIS")) {
				return TypeParameterBuilder.get(NamedType.THIS);
			}
			return TypeParameterBuilder.get(typeParameter);
		}

		return TypeParameterBuilder.get(parameterType);
	}
	
	public NamedType toType(String className) {

		TypeElement typeElement = elements.getTypeElement(className);

		String genericType = null;
		
		if (typeElement == null) {
			int genericTypeIndex = className.indexOf("<");
			if (genericTypeIndex != -1) {
				genericType = className.substring(genericTypeIndex + 1);
				genericType = genericType.substring(0, genericType.length() - 1);
				className = className.substring(0,genericTypeIndex);
				typeElement = elements.getTypeElement(className);
			}
		}
		
		if (typeElement != null) {

			if (genericType != null) {
				String[] params = genericType.split(",");
				TypeParameter[] parameters = new TypeParameter[params.length];
				for (int i = 0; i < params.length; i++) {
					parameters[i] = toTypeParameter(params[i]);
				}

				PackageElement packageElement = elements.getPackageOf(typeElement);
				return TypedClassBuilder.get(packageElement.getQualifiedName().toString(), typeElement.getSimpleName().toString(), parameters);

			} else {
				return toType(typeElement.asType());
			}
		}

		return null;
	}
}