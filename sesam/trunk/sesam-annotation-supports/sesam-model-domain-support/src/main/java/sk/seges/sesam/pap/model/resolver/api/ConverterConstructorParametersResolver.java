package sk.seges.sesam.pap.model.resolver.api;

import sk.seges.sesam.core.pap.model.ParameterElement;

public interface ConverterConstructorParametersResolver {

	public static final String THIS = "this";

	ParameterElement[] getConstructorAditionalParameters();
	
}