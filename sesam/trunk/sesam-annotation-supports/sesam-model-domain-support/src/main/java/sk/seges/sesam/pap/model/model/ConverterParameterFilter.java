package sk.seges.sesam.pap.model.model;

import java.util.LinkedList;
import java.util.List;

import sk.seges.sesam.core.pap.model.ConverterConstructorParameter;

public enum ConverterParameterFilter {

	NAME {
		@Override
		protected boolean equalsBy(ConverterConstructorParameter parameter1, ConverterConstructorParameter parameter2) {
			return parameter1.getName().equals(parameter2.getName());
		}
	},
	ALIAS_NAME {
		@Override
		protected boolean equalsBy(ConverterConstructorParameter parameter1, ConverterConstructorParameter parameter2) {
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
		protected boolean equalsBy(ConverterConstructorParameter parameter1, ConverterConstructorParameter parameter2) {
			return parameter1.getType().equals(parameter2.getType());
		}
	},
//	CONVERTER {
//		@Override
//		protected boolean equalsBy(ConverterParameter parameter1, ConverterParameter parameter2) {
//			return parameter1.getConverter().equals(parameter2.getConverter());
//		}	}
	;

	public List<ConverterConstructorParameter> filterBy(List<ConverterConstructorParameter> parameters, ConverterConstructorParameter parameter) {
		return by(parameters, parameter, true);
	}
	
	public List<ConverterConstructorParameter> excludeBy(List<ConverterConstructorParameter> parameters, ConverterConstructorParameter... params) {
		for (ConverterConstructorParameter parameter: params) {
			parameters = by(parameters, parameter, false);
		}
		return parameters;
	}
	
	private List<ConverterConstructorParameter> by(List<ConverterConstructorParameter> parameters, ConverterConstructorParameter parameter, boolean condition) {
		List<ConverterConstructorParameter> result = new LinkedList<ConverterConstructorParameter>();
		for (ConverterConstructorParameter converterParameter : parameters) {
			if (equalsBy(converterParameter, parameter) == condition) {
				result.add(converterParameter);
			}
		}

		return result;
	}

	protected abstract boolean equalsBy(ConverterConstructorParameter parameter1, ConverterConstructorParameter parameter2);
}