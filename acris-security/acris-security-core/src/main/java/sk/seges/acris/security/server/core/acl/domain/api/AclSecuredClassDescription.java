package sk.seges.acris.security.server.core.acl.domain.api;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;
import sk.seges.sesam.model.metadata.strategy.DBPropertyConverter;
import sk.seges.sesam.model.metadata.strategy.PojoPropertyConverter;

@BeanWrapper
@MetaModel(beanPropertyConverter = {PojoPropertyConverter.class, DBPropertyConverter.class})
public interface AclSecuredClassDescription extends IDomainObject<Long> {

	String getClassName();

	void setClassName(String className);
}
