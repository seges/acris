package sk.seges.sesam.pap.service.model;

import java.util.List;

import sk.seges.sesam.core.pap.model.ConverterConstructorParameter;

public interface ServiceConverterParametersFilter {

	List<ConverterConstructorParameter> getPropagatedParameters(List<ConverterConstructorParameter> parameters);

}