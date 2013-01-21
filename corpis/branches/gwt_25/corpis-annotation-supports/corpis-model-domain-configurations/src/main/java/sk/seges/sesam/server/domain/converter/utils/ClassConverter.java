package sk.seges.sesam.server.domain.converter.utils;

import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.shared.model.converter.ConverterProviderContext;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class ClassConverter {

	public static String getDomainClassName(ConverterProviderContext converterProviderContext, String dtoClassName) {
		if (dtoClassName == null) {
			return dtoClassName;
		}
		
		Class<?> dtoClass = null;
		try {
			dtoClass = Class.forName(dtoClassName);
		} catch (ClassNotFoundException e) {
			return dtoClassName;
		}

		DtoConverter<?, Object> converterForDto = null;
		
		if (converterProviderContext != null) {
			converterForDto = converterProviderContext.getConverterForDto(dtoClass);
		}

		if (converterForDto != null) {
			TransferObjectMapping annotation = converterForDto.getClass().getAnnotation(TransferObjectMapping.class);
			if (annotation != null) {
				return annotation.domainClassName();
			}
		}
		
		TransferObjectMapping annotation = dtoClass.getAnnotation(TransferObjectMapping.class);
		
		if (annotation == null) {
			return dtoClassName;
		}

		return annotation.domainClassName();
	}
}
