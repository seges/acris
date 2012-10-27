package sk.seges.acris.security.server.core.acl.domain.api;

import sk.seges.acris.core.client.annotation.BeanWrapper;
import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;
import sk.seges.sesam.model.metadata.strategy.DBPropertyConverter;
import sk.seges.sesam.model.metadata.strategy.PojoPropertyConverter;

@BeanWrapper
@MetaModel(beanPropertyConverter = {PojoPropertyConverter.class, DBPropertyConverter.class})
public interface AclSid extends IDomainObject<Long> {

	boolean isPrincipal();

	void setPrincipal(boolean principal);

	String getSid();

	void setSid(String sid);

}