package sk.seges.sesam.pap.service.model;

import java.util.ArrayList;
import java.util.List;

import sk.seges.sesam.core.pap.model.ConverterParameter;

public class DefaultServiceConverterParametersFilter implements ServiceConverterParametersFilter {

	public DefaultServiceConverterParametersFilter() {
	}

	public List<ConverterParameter> getPropagatedParameters(List<ConverterParameter> parameters) {
		List<ConverterParameter> result = new ArrayList<ConverterParameter>();
		for (ConverterParameter parameter : parameters) {
			if (parameter.isPropagated()) {
				result.add(parameter);
			}
		}

		return result;
	}
}