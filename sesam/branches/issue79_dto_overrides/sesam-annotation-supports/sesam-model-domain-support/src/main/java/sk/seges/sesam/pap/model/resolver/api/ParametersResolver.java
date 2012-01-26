package sk.seges.sesam.pap.model.resolver.api;

import sk.seges.sesam.core.pap.model.ParameterElement;

public interface ParametersResolver {

	ParameterElement[] getConstructorAditionalParameters();
	
}