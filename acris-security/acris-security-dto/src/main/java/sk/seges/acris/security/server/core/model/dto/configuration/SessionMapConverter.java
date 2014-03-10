package sk.seges.acris.security.server.core.model.dto.configuration;

import sk.seges.sesam.server.domain.converter.PropertyHolderToObjectConverter;
import sk.seges.sesam.server.model.converter.common.MapConverter;
import sk.seges.sesam.shared.model.api.PropertyHolder;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class SessionMapConverter extends MapConverter<String, PropertyHolder, String, Object> {

	public SessionMapConverter(ConverterProviderContext converterProviderContext) {
		super(converterProviderContext);
	}

	@Override
	protected DtoConverter<PropertyHolder, Object> getDomainValueConverter(Object o) {
		return new PropertyHolderToObjectConverter();
	}
}
