package sk.seges.sesam.core.pap.builder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
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

import sk.seges.sesam.core.pap.builder.api.NameTypes;
import sk.seges.sesam.core.pap.builder.api.TypeMirrorConverter;
import sk.seges.sesam.core.pap.model.InputClass;
import sk.seges.sesam.core.pap.model.OutputClass;
import sk.seges.sesam.core.pap.model.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.ArrayNamedType;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;

public class NameTypesUtils implements NameTypes {

	private ProcessingEnvironment processingEnv;
	
	public NameTypesUtils(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
	
	class NamedTypeMirrorConverter implements TypeMirrorConverter {

		@Override
		public NamedType handleType(TypeMirror type) {
			return toType(type);
		}
	}
	
	protected TypeMirrorConverter getTypeMirrorConverter() {
		return new NamedTypeMirrorConverter();
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
									boundTypes.add(getTypeMirrorConverter().handleType(superClassType));
								}

								for (TypeMirror typeInterface: element.getInterfaces()) {
									if (typeInterface != null && !typeInterface.toString().equals(Object.class.getCanonicalName())) {
										boundTypes.add(getTypeMirrorConverter().handleType(typeInterface));
									}
								}
							} else {
								boundTypes.add(getTypeMirrorConverter().handleType(upperBound));
							}
							typeParameters[i] = TypeParameterBuilder.get(variableName, boundTypes.toArray(new Type[] {}));
						}
					} else if (typeParameter.getKind().equals(TypeKind.WILDCARD)) {
						WildcardType wildcardType = (WildcardType)typeParameter;
						
						if (wildcardType.getExtendsBound() != null) {
							boundTypes.add(getTypeMirrorConverter().handleType(wildcardType.getExtendsBound()));
							typeParameters[i] = TypeParameterBuilder.get("?", boundTypes.toArray(new Type[] {}));
						} else if (wildcardType.getSuperBound() != null) {
							boundTypes.add(getTypeMirrorConverter().handleType(wildcardType.getSuperBound()));
							typeParameters[i] = TypeParameterBuilder.get("?", boundTypes.toArray(new Type[] {}));
						} else {
							typeParameters[i] = TypeParameterBuilder.get("?");
						}
						
					} else {
						boundTypes.add(getTypeMirrorConverter().handleType(typeParameter));
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
				
			PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(declaredType.asElement());
			return handleGenerics(new InputClass(typeMirror, packageElement.getQualifiedName().toString(), declaredType.asElement().getSimpleName().toString()), declaredType);
		case ARRAY:
			return new ArrayNamedType(getTypeMirrorConverter().handleType(((ArrayType)typeMirror).getComponentType()));
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

	public TypeMirror fromType(NamedType type) {
		if (type.asType() != null) {
			return type.asType();
		}
		
		//no package means primitive type
		if (type.getPackageName() == null) {
			for (TypeKind kind: TypeKind.values()) {
				if (kind.name().toLowerCase().equals(type.getSimpleName())) {
					return processingEnv.getTypeUtils().getPrimitiveType(kind);
				}
			}
		}
		
		if (type instanceof ArrayNamedType) {
			return processingEnv.getTypeUtils().getArrayType(fromType(((ArrayNamedType)type).getComponentType()));
		}

		if (type instanceof HasTypeParameters && ((HasTypeParameters)type).getTypeParameters() != null) {
			List<TypeMirror> typeArgs = new ArrayList<TypeMirror>();

			TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(type.getCanonicalName());
			
			int i = 0;
			for (TypeParameter typeParameter: ((HasTypeParameters)type).getTypeParameters()) {
				if (typeParameter.getVariable() == null || typeParameter.getVariable().length() == 0) {
					//no variable
					if (typeParameter.getBounds() != null && typeParameter.getBounds().length > 0) {
						if (typeParameter.getBounds().length == 1) {
							typeArgs.add(fromType(toImmutableType(typeParameter.getBounds()[0].getUpperBound())));
						} else {
							//TODO what if there is no variable and more bounds ?!, like List<Serializable & Comparable>
						}
					} else {
						//TODO no variable and no bounds? Can we use Object instead?
					}
				} else if (typeParameter.equals("?")) {
					if (typeParameter.getBounds() != null && typeParameter.getBounds().length > 0) {
						typeArgs.add(processingEnv.getTypeUtils().getWildcardType(fromType(toType(typeParameter.getBounds()[0].getUpperBound())), null));
					} else {
						typeArgs.add(processingEnv.getTypeUtils().getWildcardType(null, null));
					}
				} else {
					typeArgs.add(typeElement.getTypeParameters().get(i).asType());
				}
				i++;
			}

			return processingEnv.getTypeUtils().getDeclaredType(typeElement, typeArgs.toArray(new TypeMirror[] {}));
		}
		
		return processingEnv.getTypeUtils().getDeclaredType(processingEnv.getElementUtils().getTypeElement(type.getCanonicalName()));
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
		case ARRAY:
			return toImmutableType(typeMirror);

		case TYPEVAR:
			TypeVariable typeVariable = ((TypeVariable)typeMirror);
			
			String name = typeVariable.asElement().getSimpleName().toString();
			
			if (typeVariable.getUpperBound() != null) {
				return TypeParameterBuilder.get(name, getTypeMirrorConverter().handleType(typeVariable.getUpperBound()));
			}
			//TODO lower bound is not supported for now
			return TypeParameterBuilder.get(name);
		}
		
		throw new RuntimeException("Unsupported type " + typeMirror.getKind());
	}

	public ImmutableType toImmutableType(Element element) {
		return toImmutableType(element.asType());
	}

	public NamedType toType(Element element) {
		return getTypeMirrorConverter().handleType(element.asType());
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
			
			return TypeParameterBuilder.get(typeParameter.substring(0, extendsIndex).trim(), toType(parameterType));
		}

		NamedType parameterType = toType(typeParameter);

		if (parameterType == null) {
			return TypeParameterBuilder.get(typeParameter);
		}

		return TypeParameterBuilder.get(parameterType);
	}
	
	public ImmutableType toImmutableType(String className) {
		TypeElement typeElement = processingEnv.getElementUtils().getTypeElement(className);

		String genericType = null;
		
		if (typeElement == null) {
			int genericTypeIndex = className.indexOf("<");
			if (genericTypeIndex != -1) {
				genericType = className.substring(genericTypeIndex + 1);
				genericType = genericType.substring(0, genericType.length() - 1);
				className = className.substring(0,genericTypeIndex);
				typeElement = processingEnv.getElementUtils().getTypeElement(className);
			}
		}
		
		if (typeElement != null) {

			if (genericType != null) {
				String[] params = genericType.split(",");
				TypeParameter[] parameters = new TypeParameter[params.length];
				for (int i = 0; i < params.length; i++) {
					parameters[i] = toTypeParameter(params[i]);
				}

				PackageElement packageElement = processingEnv.getElementUtils().getPackageOf(typeElement);
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
	
	public ImmutableType erasure(NamedType namedType) {
		if (namedType instanceof HasTypeParameters && ((HasTypeParameters) namedType).getTypeParameters() != null) {
			return new OutputClass(namedType.asType(), namedType.getPackageName(), namedType.getSimpleName());
		}

		if (namedType instanceof ImmutableType) {
			return (ImmutableType)namedType;
		}

		return new OutputClass(namedType.asType(), namedType.getPackageName(), namedType.getSimpleName());
	}
}