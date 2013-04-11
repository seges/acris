package sk.seges.sesam.server.domain.converter;

import sk.seges.sesam.server.model.converter.ClassConverter;
import sk.seges.sesam.shared.model.converter.BasicConverter;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;

public class ProjectableResultConverter extends BasicConverter<String, String> {

	private final ConverterProviderContext converterProviderContext;
	
	public ProjectableResultConverter(ConverterProviderContext converterProviderContext) {
		this.converterProviderContext = converterProviderContext;
	}
	
	@Override
	public String convertToDto(String result, String domain) {
		return domain;
	}

	@Override
	public String toDto(String domain) {
		return domain;
	}

	@Override
	public String convertFromDto(String result, String dto) {
		return fromDto(dto);
	}

	@Override
	public String fromDto(String dto) {
		return ClassConverter.getDomainClassName(converterProviderContext, dto);
	}
}