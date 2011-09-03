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
import sk.seges.sesam.core.pap.model.TypeParameterBuilder;
import sk.seges.sesam.core.pap.model.TypedClassBuilder;
import sk.seges.sesam.core.pap.model.api.HasTypeParameters;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.model.api.TypeParameter;
import sk.seges.sesam.core.pap.model.api.TypeVariable;


public class TypeParametersSupport {

	private ProcessingEnvironment processingEnv;
	private NameTypesUtils nameTypesUtils;
	
	public TypeParametersSupport(ProcessingEnvironment processingEnv, NameTypesUtils nameTypesUtils) {
		this.processingEnv = processingEnv;
		this.nameTypesUtils = nameTypesUtils;
	};
	
	public boolean hasTypeParameters(NamedType type) {
		if (!(type instanceof HasTypeParameters)) {
			return false;
		}
		return ((HasTypeParameters)type).getTypeParameters() != null;
	}
	
	public boolean hasVariableParameterTypes(NamedType type) {
		if (type instanceof HasTypeParameters && ((HasTypeParameters) type).getTypeParameters() != null) {
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
		
		if (((HasTypeParameters) type).getTypeParameters() != null) {
			for (TypeParameter typeParameter: ((HasTypeParameters)type).getTypeParameters()) {
				if (typeParameter.getVariable() != null && typeParameter.getVariable().length() > 0) {
					result.add(typeParameter);
				}
			}
		}
		
		return result.toArray(new TypeParameter[] {});
	}
	
	public Type applyTypeParameters(Type type, DeclaredType declaredType) {
		if (declaredType.getTypeArguments() != null && declaredType.getTypeArguments().size() > 0) {
			TypeParameter[] variables = new TypeParameter[declaredType.getTypeArguments().size()];
			
			int i = 0;
			for (TypeMirror typeArgumentMirror: declaredType.getTypeArguments()) {
				variables[i++] = TypeParameterBuilder.get(nameTypesUtils.toType(typeArgumentMirror));
			}
			return TypedClassBuilder.get(type, variables);
		}
		
		return type;
	}
	
	public Type applyUpperTypeParameters(Type type, TypeElement typeElement) {
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

	public enum RenameActionType {
		PREFIX {
			@Override
			public String apply(String name, String parameter) {
				return parameter + name;
			}
		}, 
		REPLACE {
			@Override
			public String apply(String name, String parameter) {
				return parameter;
			}
		}, 
		SUFIX {
			@Override
			public String apply(String name, String parameter) {
				return name + parameter;
			}
		};
		
		public abstract String apply(String name, String parameter);
	}

	public ImmutableType prefixTypeParameter(ImmutableType type, String prefix) {
		if (hasTypeParameters(type)) {
			return renameTypeParameter((HasTypeParameters)type, RenameActionType.PREFIX, prefix + "_");
		}
		return type;
	}

	public HasTypeParameters renameTypeParameter(HasTypeParameters type, RenameActionType actionType, String parameter) {
		return renameTypeParameter(type, actionType, parameter, null, false);
	}
	
	public HasTypeParameters renameTypeParameter(HasTypeParameters type, RenameActionType actionType, String parameter, String oldName, boolean recursive) {	
			
		if (type == null || type.getTypeParameters() == null || type.getTypeParameters().length == 0) {
			return type;
		}
		
		List<TypeParameter> typeParameters = new ArrayList<TypeParameter>();
		
		for (TypeParameter typeParameter: type.getTypeParameters()) {
			String variable = typeParameter.getVariable();
			
			if (variable != null && !variable.equals("?")) {
				if (oldName == null || oldName.equals(variable)) {
					variable = actionType.apply(variable, parameter);
				}
				typeParameters.add(TypeParameterBuilder.get(variable/*, typeParameter.getBounds()*/));
			} else if (recursive) {
				TypeVariable[] bounds = typeParameter.getBounds();
				if (bounds != null && bounds.length > 0) {
				
					Type[] renamedBounds = new Type[bounds.length];

					int i = 0;
					for (TypeVariable typeVariable: bounds) {
						Type upperBound = typeVariable.getUpperBound();
						if (upperBound instanceof HasTypeParameters && ((HasTypeParameters) upperBound).getTypeParameters() != null) {
							upperBound = renameTypeParameter((HasTypeParameters)upperBound, actionType, parameter, oldName, recursive);
						}
						
						renamedBounds[i] = upperBound;
						i++;
					}
					typeParameters.add(TypeParameterBuilder.get(variable, renamedBounds));
				} else {
					typeParameters.add(TypeParameterBuilder.get(variable));
				}
			}
		}
		
		return TypedClassBuilder.get(nameTypesUtils.erasure(type), typeParameters.toArray(new TypeParameter[] {}));
	}
	
	public ImmutableType applyVariableTypeParameters(ImmutableType type, DeclaredType declaredType) {
		if (declaredType.getTypeArguments() != null && declaredType.getTypeArguments().size() > 0) {
			TypeParameter[] variables = new TypeParameter[declaredType.getTypeArguments().size()];
			int i = 0;
			for (TypeMirror typeParameterElement: declaredType.getTypeArguments()) {
				if (typeParameterElement.getKind().equals(TypeKind.TYPEVAR)) {
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

	public Integer getParameterIndexByName(DeclaredType declaredType, String name) {
		if (declaredType == null) {
			return null;
		}

		int index = 0;
		for (TypeMirror typeParameter: declaredType.getTypeArguments()) {
			if (typeParameter.getKind().equals(TypeKind.TYPEVAR)) {
				javax.lang.model.type.TypeVariable typeVariable = (javax.lang.model.type.TypeVariable)typeParameter;
				if (typeVariable.asElement().getSimpleName().toString().equals(name)) {
					return index;
				}
			}
			index++;
		}
		
		return null;
	}
	
	public boolean hasParameterByName(DeclaredType declaredType, String name) {
		return getParameterIndexByName(declaredType, name) != null;
	}
	
	public NamedType stripTypesFromTypeParameters(NamedType type) {
		if (type instanceof HasTypeParameters && ((HasTypeParameters) type).getTypeParameters() != null) {
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
							bounds[j++] = nameTypesUtils.toType(boundType.getUpperBound());
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