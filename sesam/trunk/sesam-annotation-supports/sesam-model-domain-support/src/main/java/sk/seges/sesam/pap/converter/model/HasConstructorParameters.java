package sk.seges.sesam.pap.converter.model;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;

public interface HasConstructorParameters extends MutableDeclaredType {

	ParameterElement[] getConverterParameters(ConverterConstructorParametersResolver parametersResolver);

}
