package sk.seges.sesam.server.domain.converter;

import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.shared.model.converter.BasicConverter;

public class ProjectableResultConverter extends BasicConverter<String, String> {

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
		if (dto == null) {
			return dto;
		}
		
		Class<?> dtoClass = null;
		try {
			dtoClass = Class.forName(dto);
		} catch (ClassNotFoundException e) {
			return dto;
		}
		
		TransferObjectMapping annotation = dtoClass.getAnnotation(TransferObjectMapping.class);
		
		if (annotation == null) {
			return dto;
		}
		
		return annotation.domainClassName();
	}
}