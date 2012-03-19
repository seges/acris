package sk.seges.sesam.core.pap.utils;

import java.lang.reflect.Type;

import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;


public class TypeParametersSupport {

	private MutableProcessingEnvironment processingEnv;
	
	public TypeParametersSupport(MutableProcessingEnvironment processingEnv) {
		this.processingEnv = processingEnv;
	};
		
	//TODO move to the MDT
	public Type applyTypeParameters(Type type, DeclaredType declaredType) {
		if (declaredType.getTypeArguments() != null && declaredType.getTypeArguments().size() > 0) {
			MutableTypeVariable[] variables = new MutableTypeVariable[declaredType.getTypeArguments().size()];
			
			int i = 0;
			for (TypeMirror typeArgumentMirror: declaredType.getTypeArguments()) {
				variables[i++] = processingEnv.getTypeUtils().getTypeVariable(null, processingEnv.getTypeUtils().toMutableType(typeArgumentMirror));
			}
			return processingEnv.getTypeUtils().getDeclaredType((MutableDeclaredType)processingEnv.getTypeUtils().toMutableType(type), variables);
		}
		
		return type;
	}
	
	//TODO move to the MDT
	public MutableDeclaredType applyVariableTypeParameters(MutableDeclaredType type, DeclaredType declaredType) {
		if (declaredType.getTypeArguments() != null && declaredType.getTypeArguments().size() > 0) {
			MutableTypeVariable[] variables = new MutableTypeVariable[declaredType.getTypeArguments().size()];
			int i = 0;
			for (TypeMirror typeParameterElement: declaredType.getTypeArguments()) {
				if (typeParameterElement.getKind().equals(TypeKind.TYPEVAR)) {
					variables[i] = processingEnv.getTypeUtils().getTypeVariable(typeParameterElement.toString());
				} else {
					variables[i] = processingEnv.getTypeUtils().getWildcardType((MutableTypeMirror)null, null);
				}
				i++;
			}
			return processingEnv.getTypeUtils().getDeclaredType(type, variables);
		}
		
		return type;
	}

	//TODO move to the MDT
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
	
	//TODO move to the MDT
	public boolean hasParameterByName(DeclaredType declaredType, String name) {
		return getParameterIndexByName(declaredType, name) != null;
	}
	
}