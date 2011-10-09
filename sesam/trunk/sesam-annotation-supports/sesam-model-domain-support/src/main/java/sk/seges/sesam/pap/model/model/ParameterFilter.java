package sk.seges.sesam.pap.model.model;

import java.util.LinkedList;
import java.util.List;

public enum ParameterFilter {

	NAME {
		@Override
		protected boolean equalsBy(ConverterParameter parameter1, ConverterParameter parameter2) {
			return parameter1.getName().equals(parameter2.getName());
		}
	},
	TYPE {
		@Override
		protected boolean equalsBy(ConverterParameter parameter1, ConverterParameter parameter2) {
			return parameter1.getType().equals(parameter2.getType());
		}
	},
	CONVERTER {
		@Override
		protected boolean equalsBy(ConverterParameter parameter1, ConverterParameter parameter2) {
			return parameter1.getConverter().equals(parameter2.getConverter());
		}
	};

	public List<ConverterParameter> filterBy(List<ConverterParameter> parameters, ConverterParameter parameter) {
		List<ConverterParameter> result = new LinkedList<ConverterParameter>();
		for (ConverterParameter converterParameter : parameters) {
			if (equalsBy(converterParameter, parameter)) {
				result.add(converterParameter);
			}
		}

		return result;
	}

	protected abstract boolean equalsBy(ConverterParameter parameter1, ConverterParameter parameter2);
}