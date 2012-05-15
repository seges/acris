package sk.seges.sesam.core.model.converter;

import sk.seges.sesam.dao.Page;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;
import sk.seges.sesam.server.domain.converter.ProjectableResultConverter;

@TransferObjectMapping(domainClass = Page.class, dtoClass = Page.class)
public interface PageConfiguration {

	@TransferObjectMapping(converter = ProjectableResultConverter.class)
	String projectableResult();
}
