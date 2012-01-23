package sk.seges.sesam.pap.service.model;

import java.util.ArrayList;
import java.util.List;

import sk.seges.sesam.pap.model.model.ConverterParameter;

public class ConverterParametersFilter implements ParametersFilter {

	public ConverterParametersFilter() {
	}
	
	public List<ConverterParameter> getPropagatedParameters(List<ConverterParameter> parameters) {
		List<ConverterParameter> result = new ArrayList<ConverterParameter>();
		for (ConverterParameter parameter: parameters) {
 			if (parameter.isPropagated()) {
 				result.add(parameter);
 			}
		}

		return result;
	}
}