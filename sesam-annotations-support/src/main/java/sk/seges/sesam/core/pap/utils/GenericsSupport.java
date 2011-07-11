package sk.seges.sesam.core.pap.utils;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.builder.NameTypesUtils;
import sk.seges.sesam.core.pap.builder.api.NameTypes;
import sk.seges.sesam.core.pap.model.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.model.api.TypeVariable;


public class GenericsSupport {

	private ProcessingEnvironment processingEnv;
	
	public GenericsSupport() {};
	
	public void init(ProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	}
	
	protected NameTypes getNameTypes() {
		return new NameTypesUtils(processingEnv.getElementUtils());
	}

	public boolean hasVariableParameterTypes(NamedType type) {
		if (type instanceof HasTypeParameters) {
			for (TypeParameter typeParameter: ((HasTypeParameters)type).getTypeParameters()) {
				if (typeParameter.getVariable() != null && typeParameter.getVariable().length() > 0) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public TypeParameter[] getVariableParameterTypes(HasTypeParameters type) {
		List<TypeParameter> result = new ArrayList<TypeParameter>();
		
		for (TypeParameter typeParameter: ((HasTypeParameters)type).getTypeParameters()) {
			if (typeParameter.getVariable() != null && typeParameter.getVariable().length() > 0) {
				result.add(typeParameter);
			}
		}
		
		return result.toArray(new TypeParameter[] {});
	}
	
	public Type applyGenerics(Type type, DeclaredType declaredType) {
		if (declaredType.getTypeArguments() != null && declaredType.getTypeArguments().size() > 0) {
			TypeParameter[] variables = new TypeParameter[declaredType.getTypeArguments().size()];
			
			int i = 0;
			for (TypeMirror typeArgumentMirror: declaredType.getTypeArguments()) {
				variables[i++] = TypeParameterBuilder.get(getNameTypes().toType(typeArgumentMirror));
			}
			return TypedClassBuilder.get(type, variables);
		}
		
		return type;
	}
	
	public Type applyUpperGenerics(Type type, TypeElement typeElement) {
		if (typeElement.getTypeParameters() != null && typeElement.getTypeParameters().size() > 0) {
			TypeParameter[] variables = new TypeParameter[typeElement.getTypeParameters().size()];
			int i = 0;
			for (TypeParameterElement typeParameterElement: typeElement.getTypeParameters()) {
				if (typeParameterElement.getBounds() != null && typeParameterElement.getBounds().size() == 1) {
					Element element = processingEnv.getTypeUtils().asElement(typeParameterElement.getBounds().get(0));
					if (element.getKind().equals(ElementKind.CLASS) ||
						element.getKind().equals(ElementKind.INTERFACE)) {
						TypeElement boundType = (TypeElement)element;
						variables[i] = TypeParameterBuilder.get(boundType.toString());
					} else {
						variables[i] = TypeParameterBuilder.get("?");
					}
				} else {
					variables[i] = TypeParameterBuilder.get("?");
				}
				i++;
			}
			return TypedClassBuilder.get(type, variables);
		}
		
		return type;
	}
	
	public Type applyVariableGenerics(Type type, TypeElement typeElement) {
		if (typeElement.getTypeParameters() != null && typeElement.getTypeParameters().size() > 0) {
			TypeParameter[] variables = new TypeParameter[typeElement.getTypeParameters().size()];
			int i = 0;
			for (TypeParameterElement typeParameterElement: typeElement.getTypeParameters()) {
				if (typeParameterElement.asType().getKind().equals(TypeKind.TYPEVAR)) {
					variables[i] = TypeParameterBuilder.get(typeParameterElement.toString());
				} else {
					variables[i] = TypeParameterBuilder.get("?");
				}
				i++;
			}
			return TypedClassBuilder.get(type, variables);
		}
		
		return type;
	}

	public NamedType stripTypesFromTypeParameters(NamedType type) {
		if (type instanceof HasTypeParameters) {
			HasTypeParameters typedType = (HasTypeParameters)type;
			if (typedType.getTypeParameters() != null && typedType.getTypeParameters().length > 0) {
				TypeParameter[] variables = new TypeParameter[typedType.getTypeParameters().length];
				int i = 0;
				for (TypeParameter typeParameter: typedType.getTypeParameters()) {
					if (typeParameter.getVariable() != null) {
						variables[i] = TypeParameterBuilder.get(typeParameter.getVariable().toString());
					} else if (typeParameter.getBounds() != null && typeParameter.getBounds().length > 0) {
						
						Type[] bounds = new Type[typeParameter.getBounds().length];
					
						int j = 0;
						for (TypeVariable boundType: typeParameter.getBounds()) {
							bounds[j++] = getNameTypes().toType(boundType.getUpperBound());
						}
						
						variables[i] = TypeParameterBuilder.get(bounds);
					} else {
						variables[i] = TypeParameterBuilder.get("?");
					}
					i++;
				}
				return TypedClassBuilder.get(type, variables);
			}
		}
		return type;
	}
}