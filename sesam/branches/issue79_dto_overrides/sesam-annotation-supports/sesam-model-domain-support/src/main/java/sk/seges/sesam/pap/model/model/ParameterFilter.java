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
	ALIAS_NAME {
		@Override
		protected boolean equalsBy(ConverterParameter parameter1, ConverterParameter parameter2) {
			if (parameter1.getSameParameter() != null) {
				if (parameter1.getSameParameter().getName().equals(parameter2.getName())) {
					return true;
				}
				
				if (parameter2.getSameParameter() != null) {
					if (parameter1.getSameParameter().getName().equals(parameter2.getSameParameter().getName())) {
						return true;
					}
				}
			}

			if (parameter2.getSameParameter() != null) {
				if (parameter1.getName().equals(parameter2.getSameParameter().getName())) {
					return true;
				}
			}

			return parameter1.getName().equals(parameter2.getName());
		}
	},
	TYPE {
		@Override
		protected boolean equalsBy(ConverterParameter parameter1, ConverterParameter parameter2) {
			return parameter1.getType().equals(parameter2.getType());
		}
	},
//	CONVERTER {
//		@Override
//		protected boolean equalsBy(ConverterParameter parameter1, ConverterParameter parameter2) {
//			return parameter1.getConverter().equals(parameter2.getConverter());
//		}	}
	;

	public List<ConverterParameter> filterBy(List<ConverterParameter> parameters, ConverterParameter parameter) {
		return by(parameters, parameter, true);
	}
	
	public List<ConverterParameter> excludeBy(List<ConverterParameter> parameters, ConverterParameter... params) {
		for (ConverterParameter parameter: params) {
			parameters = by(parameters, parameter, false);
		}
		return parameters;
	}
	
	private List<ConverterParameter> by(List<ConverterParameter> parameters, ConverterParameter parameter, boolean condition) {
		List<ConverterParameter> result = new LinkedList<ConverterParameter>();
		for (ConverterParameter converterParameter : parameters) {
			if (equalsBy(converterParameter, parameter) == condition) {
				result.add(converterParameter);
			}
		}

		return result;
	}

	protected abstract boolean equalsBy(ConverterParameter parameter1, ConverterParameter parameter2);
}