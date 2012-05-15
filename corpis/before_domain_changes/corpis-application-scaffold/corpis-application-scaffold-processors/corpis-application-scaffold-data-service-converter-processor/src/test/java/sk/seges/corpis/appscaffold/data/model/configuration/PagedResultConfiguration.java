package sk.seges.corpis.appscaffold.data.model.configuration;

import sk.seges.sesam.dao.PagedResult;
import sk.seges.sesam.pap.model.annotation.TransferObjectMapping;

@TransferObjectMapping(domainClass = PagedResult.class, dtoClass = PagedResult.class)
public interface PagedResultConfiguration {
}