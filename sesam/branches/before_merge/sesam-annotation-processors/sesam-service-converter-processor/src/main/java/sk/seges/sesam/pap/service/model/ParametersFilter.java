package sk.seges.sesam.pap.service.model;

import java.util.List;

import sk.seges.sesam.pap.model.model.ConverterParameter;

public interface ParametersFilter {

	List<ConverterParameter> getPropagatedParameters(List<ConverterParameter> parameters);

}
