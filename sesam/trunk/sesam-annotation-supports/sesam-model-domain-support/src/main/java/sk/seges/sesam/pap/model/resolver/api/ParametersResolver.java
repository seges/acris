package sk.seges.sesam.pap.model.resolver.api;

import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.ParameterElement;

public interface ParametersResolver {

	ParameterElement[] getConstructorAditionalParameters(TypeMirror domainType);
}