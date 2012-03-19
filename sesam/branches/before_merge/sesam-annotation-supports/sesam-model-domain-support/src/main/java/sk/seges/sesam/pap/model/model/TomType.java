package sk.seges.sesam.pap.model.model;

enum TomType {
	
	DTO_DEFINED {
		@Override
		boolean appliesFor(ConfigurationTypeElement configuration) {
			return configuration.hasDtoSpecified();
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
			return configuration.hasConverterSpecified();
		}
	},
	CONVERTER_GENERATED {
		@Override
		boolean appliesFor(ConfigurationTypeElement configuration) {
			return configuration.hasGeneratedConverter();
		}
	};
	
	abstract boolean appliesFor(ConfigurationTypeElement configuration);
}