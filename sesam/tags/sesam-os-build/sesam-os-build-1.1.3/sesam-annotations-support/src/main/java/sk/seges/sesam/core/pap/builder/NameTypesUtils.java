package sk.seges.sesam.core.pap.builder;

import java.lang.reflect.Type;

import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import sk.seges.sesam.core.pap.builder.api.NameTypes;
import sk.seges.sesam.core.pap.model.InputClass;
import sk.seges.sesam.core.pap.model.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;

public class NameTypesUtils implements NameTypes {

	private Elements elements;
	private Types types;
	
	public NameTypesUtils(Elements elements, Types types) {
		this.elements = elements;
		this.types = types;
	}
	
	private boolean isDeclaredType(Element element) {
		return (element.getKind().equals(ElementKind.CLASS) ||
				element.getKind().equals(ElementKind.INTERFACE));
	}
	
	private MutableType handleGenerics(MutableType simpleType, Element element) {
		if (element.getKind().equals(ElementKind.CLASS) || element.getKind().equals(ElementKind.INTERFACE)) {
			TypeElement typeElement = (TypeElement)element;
			if (typeElement.getTypeParameters() != null && typeElement.getTypeParameters().size() > 0) {
				TypeParameter[] typeParameters = new TypeParameter[typeElement.getTypeParameters().size()];
				for (int i = 0; i < typeElement.getTypeParameters().size(); i++) {
					TypeParameterElement typeParameterElement = typeElement.getTypeParameters().get(i);
					Type[] boundTypes = new Type[typeParameterElement.getBounds().size()];
					for (int j = 0; j < typeParameterElement.getBounds().size(); j++) {
						TypeMirror typeMirror = typeParameterElement.getBounds().get(j);
						TypeVariable typeVariable = (TypeVariable)typeParameterElement.asType();
						typeMirror = typeVariable.getUpperBound();
						Element boundElement = types.asElement(typeMirror);
						boundTypes[j] = toType(boundElement);
					}
					if (typeParameterElement.asType().getKind().equals(TypeKind.TYPEVAR)) {
						TypeVariable typeVariable = (TypeVariable)typeParameterElement.asType();
						typeParameters[i] = TypeParameterBuilder.get(typeVariable.toString(), boundTypes);
					} else {
						typeParameters[i] = TypeParameterBuilder.get(boundTypes);
					}
				}
				return TypedClassBuilder.get(simpleType, typeParameters);
			}
		}
		
		return simpleType;
	}
	
	public MutableType toType(Element element) {
		if (element.getEnclosingElement() != null && isDeclaredType(element.getEnclosingElement())) {
			MutableType enclosedElement = toType(element.getEnclosingElement());
			return handleGenerics(new InputClass(enclosedElement, element.getSimpleName().toString()), element);
		}

		PackageElement packageElement = elements.getPackageOf(element);
		return handleGenerics(new InputClass(packageElement.getQualifiedName().toString(), element.getSimpleName().toString()), element);
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
				return toType(typeElement);
			}
		}

		return null;
	}
}