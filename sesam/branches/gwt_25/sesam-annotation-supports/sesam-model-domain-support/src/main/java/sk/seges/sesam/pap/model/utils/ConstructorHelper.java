package sk.seges.sesam.pap.model.utils;

import java.util.LinkedList;
import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.ConstructorParameter;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;

public class ConstructorHelper {

	public static ConstructorParameter toConverterParameter(MutableTypes typeUtils, VariableElement constructorParameter) {
		return new ConstructorParameter(typeUtils.toMutableType(constructorParameter.asType()), constructorParameter.getSimpleName().toString());
	}

	public static List<ConstructorParameter> getConstructorParameters(MutableTypes typeUtils, ExecutableElement constructor) {
		List<ConstructorParameter> result = new LinkedList<ConstructorParameter>();

		//Strange java bug
		try {
			List<? extends VariableElement> parameters = constructor.getParameters();
			for (VariableElement parameterElement: parameters) {
				result.add(toConverterParameter(typeUtils, parameterElement));
			}
			
			return result;
		} catch (Exception e) {
		}

		List<? extends TypeMirror> parameterTypes = ((ExecutableType)constructor.asType()).getParameterTypes();
		
		int i = 0;
		for (TypeMirror parameterType: parameterTypes) {
			result.add(new ConstructorParameter(typeUtils.toMutableType(parameterType), "arg" + i++));
		}
		
		return result;
	}
}