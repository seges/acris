package sk.seges.sesam.core.pap.builder;

import java.lang.reflect.Type;

import javax.lang.model.element.Element;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import sk.seges.sesam.core.pap.builder.api.NameTypes;
import sk.seges.sesam.core.pap.model.InputClass;
import sk.seges.sesam.core.pap.model.InputClass.TypeParameter;
import sk.seges.sesam.core.pap.model.InputClass.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.InputClass.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.MutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;

public class NameTypesUtils implements NameTypes {

	private Elements elements;
	
	public NameTypesUtils(Elements elements) {
		this.elements = elements;
	}
	
	public MutableType toType(Element element) {
		PackageElement packageElement = elements.getPackageOf(element);
		return new InputClass(packageElement.getQualifiedName().toString(), element.getSimpleName().toString());
	}
	
	private NamedType toType(Class<?> clazz) {
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