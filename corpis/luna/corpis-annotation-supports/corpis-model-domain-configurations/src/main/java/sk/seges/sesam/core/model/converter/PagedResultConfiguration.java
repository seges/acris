package sk.seges.sesam.core.model.converter;

import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = PagedResult.class, dtoClass = PagedResult.class)
public class PagedResultConfiguration {

}
