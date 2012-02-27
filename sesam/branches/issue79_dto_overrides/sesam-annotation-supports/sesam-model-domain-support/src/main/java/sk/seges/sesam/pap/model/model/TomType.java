package sk.seges.sesam.pap.model.model;

enum TomType {

	DTO_DEFINED {
		@Override
		boolean appliesFor(ConfigurationTypeElement configuration) {
			return configuration.hasDtoSpecified();
		}
	},
	DTO_NOT_DEFINED {
		@Override
		boolean appliesFor(ConfigurationTypeElement configuration) {
			return !configuration.hasDtoSpecified();
		}
	},
	DOMAIN {
		@Override
		boolean appliesFor(ConfigurationTypeElement configuration) {
			return configuration.hasDomainSpecified();
		}
	},
	CONVERTER_DEFINED {
		@Override
		boolean appliesFor(ConfigurationTypeElement configuration) {
			return configuration.getDelegateConfigurationTypeElement() == null && configuration.hasConverterSpecified();
		}
	},
	CONVERTER_GENERATED {
		@Override
		boolean appliesFor(ConfigurationTypeElement configuration) {
			return configuration.hasGeneratedConverter() && configuration.getDelegateConfigurationTypeElement() == null;
		}
	};
	
	abstract boolean appliesFor(ConfigurationTypeElement configuration);

	public static boolean appliesFor(ConfigurationTypeElement configuration, TomType... types) {
		if (types == null) {
			return false;
		}
		
		for (TomType tomType: types) {
			if (!tomType.appliesFor(configuration)) {
				return false;
			}
		}
		return true;
	}
}