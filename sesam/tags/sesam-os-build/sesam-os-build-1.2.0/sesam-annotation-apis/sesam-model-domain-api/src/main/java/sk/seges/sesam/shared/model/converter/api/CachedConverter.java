package sk.seges.sesam.shared.model.converter.api;

import java.io.Serializable;

public interface CachedConverter<DTO, DOMAIN> extends DtoConverter<DTO, DOMAIN> {

	DTO createDtoInstance(Object domainSource, Serializable id);
	DOMAIN createDomainInstance(Object dtoSource, Serializable id);

	DOMAIN getDomainInstance(Object dtoSource, Serializable id);
	DTO getDtoInstance(Object domainSource, Serializable id);
}