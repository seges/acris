package sk.seges.sesam.core.pap.builder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.type.WildcardType;
import javax.lang.model.util.Elements;

import sk.seges.sesam.core.pap.builder.api.NameTypes;
import sk.seges.sesam.core.pap.model.InputClass;
import sk.seges.sesam.core.pap.model.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.ArrayNamedType;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;

public class NameTypesUtils implements NameTypes {

	private Elements elements;
	
	public NameTypesUtils(Elements elements) {
		this.elements = elements;
	}
	
	private ImmutableType handleGenerics(ImmutableType simpleType, TypeMirror type) {
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

						String variableName = typeVariable.asElement().getSimpleName().toString();
						
						if (variableName != null && variableName.length() > 0 && !variableName.startsWith("?")) {
							typeParameters[i] = TypeParameterBuilder.get(variableName);
						} else if (upperBound != null && upperBound.getKind().equals(TypeKind.DECLARED)) {
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
							typeParameters[i] = TypeParameterBuilder.get(variableName, boundTypes.toArray(new Type[] {}));
						}
					} else if (typeParameter.getKind().equals(TypeKind.WILDCARD)) {
						WildcardType wildcardType = (WildcardType)typeParameter;
						
						if (wildcardType.getExtendsBound() != null) {
							boundTypes.add(toType(wildcardType.getExtendsBound()));
							typeParameters[i] = TypeParameterBuilder.get("?", boundTypes.toArray(new Type[] {}));
						} else if (wildcardType.getSuperBound() != null) {
							boundTypes.add(toType(wildcardType.getSuperBound()));
							typeParameters[i] = TypeParameterBuilder.get("?", boundTypes.toArray(new Type[] {}));
						} else {
							typeParameters[i] = TypeParameterBuilder.get("?");
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

	public ImmutableType toImmutableType(TypeMirror typeMirror) {
		switch (typeMirror.getKind()) {
		case DECLARED:
			DeclaredType declaredType = (DeclaredType)typeMirror;
			
			if (declaredType.asElement().getEnclosingElement() != null && declaredType.asElement().getEnclosingElement().asType().getKind().equals(TypeKind.DECLARED)) {
				NamedType enclosedElement = toType(declaredType.asElement().getEnclosingElement());
				return handleGenerics(new InputClass(typeMirror, enclosedElement, declaredType.asElement().getSimpleName().toString()), declaredType);
			}
				
			PackageElement packageElement = elements.getPackageOf(declaredType.asElement());
			return handleGenerics(new InputClass(typeMirror, packageElement.getQualifiedName().toString(), declaredType.asElement().getSimpleName().toString()), declaredType);
		case BOOLEAN:
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
		case VOID:
			return new InputClass(typeMirror, (String)null, typeMirror.getKind().name().toLowerCase());
		}
		
		throw new RuntimeException("Unsupported type " + typeMirror.getKind());
	}
	
	public NamedType toType(TypeMirror typeMirror) {
		switch (typeMirror.getKind()) {
		case DECLARED:
		case BOOLEAN:
		case BYTE:
		case CHAR:
		case DOUBLE:
		case FLOAT:
		case INT:
		case LONG:
		case SHORT:
		case VOID:
			return toImmutableType(typeMirror);

		case TYPEVAR:
			TypeVariable typeVariable = ((TypeVariable)typeMirror);
			
			String name = typeVariable.asElement().getSimpleName().toString();
			
			if (typeVariable.getUpperBound() != null) {
				return TypeParameterBuilder.get(name, toType(typeVariable.getUpperBound()));
			}
			//TODO lower bound is not supported for now
			return TypeParameterBuilder.get(name);
		case ARRAY:
			return new ArrayNamedType(toType(((ArrayType)typeMirror).getComponentType()));
		}
		
		throw new RuntimeException("Unsupported type " + typeMirror.getKind());
	}

	public ImmutableType toImmutableType(Element element) {
		return toImmutableType(element.asType());
	}
	
	public NamedType toType(Element element) {
		return toType(element.asType());
	}

	private static ImmutableType toType(Class<?> clazz) {
		if (clazz == null) {
			return null;
		}
		if (clazz.getEnclosingClass() != null) {
			return new InputClass(null, toType(clazz.getEnclosingClass()), clazz.getSimpleName());
		}
		return new InputClass(null, clazz.getPackage().getName(), clazz.getSimpleName());
	}

	public ImmutableType toImmutableType(Type javaType) {
		if (javaType instanceof Class) {
			return toType((Class<?>)javaType);
		}
		
		if (javaType instanceof ImmutableType) {
			return (ImmutableType)javaType;
		}

		if (javaType instanceof NamedType) {
			InputClass result = new InputClass(null, ((NamedType)javaType).getPackageName(), ((NamedType)javaType).getSimpleName());
			for (AnnotationMirror annotation: ((NamedType)javaType).getAnnotations()) {
				result.annotateWith(annotation);
			}
			return result;
		}
		
		return null;
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
	
	public ImmutableType toImmutableType(String className) {
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
				return TypedClassBuilder.get(typeElement.asType(), packageElement.getQualifiedName().toString(), typeElement.getSimpleName().toString(), parameters);

			} else {
				return toImmutableType(typeElement.asType());
			}
		}

		return null;
	}
	
	public NamedType toType(String className) {
		return toImmutableType(className);
	}
}