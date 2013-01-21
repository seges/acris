package sk.seges.sesam.pap.converter.util;

import java.util.ArrayList;
import java.util.List;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.ParametersFilter;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;

public class HasConstructorParametersDelegate {

	public ParameterElement[] getRequiredParameters(MutableProcessingEnvironment processingEnv, 
			ConverterConstructorParametersResolver parametersResolver, ConverterConstructorParametersResolver classParametersResolver) {

		ParameterElement[] allParameters = ParametersFilter.PROPAGATED.filterParameters(parametersResolver.getConstructorAditionalParameters());

		ParameterElement[] localParameters = ParametersFilter.PROPAGATED.filterParameters(classParametersResolver.getConstructorAditionalParameters());

		List<ParameterElement> generatedParameters = new ArrayList<ParameterElement>();

		for (ParameterElement parameter: allParameters) {
			if (!contains(parameter, localParameters)) {
				generatedParameters.add(parameter);
			}
		}
		
		return generatedParameters.toArray(new ParameterElement[] {});
	}
	
	private static boolean contains(ParameterElement parameter, ParameterElement[] parameters) {
		for (ParameterElement localParameter: parameters) {
			if (localParameter.getType().equals(parameter.getType())) {
				return true;
			}
		}
		
		return false;
	}
	
	public ParameterElement[] getConverterParameters(MutableProcessingEnvironment processingEnv, ConverterConstructorParametersResolver parametersResolver) {
		
//		MutableDeclaredType converterProviderType = processingEnv.getTypeUtils().toMutableType(ConverterProvider.class);

//		ParameterElement[] propagatedParameters = 
				return ParametersFilter.PROPAGATED.filterParameters(parametersResolver.getConstructorAditionalParameters());

	/*	List<ParameterElement> result = new ArrayList<ParameterElement>();
		for (ParameterElement generatedParameter: propagatedParameters) {
			if (!generatedParameter.getType().equals(converterProviderType)) {
				result.add(generatedParameter);
			}
		}
		
		return result.toArray(new ParameterElement[] {});*/
	}
}