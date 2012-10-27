package sk.seges.acris.security.server.core.acl.domain.api;

import java.io.Serializable;

import sk.seges.acris.core.client.annotation.BeanWrapper;
import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.model.metadata.annotation.MetaModel;
import sk.seges.sesam.model.metadata.strategy.DBPropertyConverter;
import sk.seges.sesam.model.metadata.strategy.PojoPropertyConverter;

@BeanWrapper
@MetaModel(beanPropertyConverter = {PojoPropertyConverter.class, DBPropertyConverter.class})
public interface AclSecuredObjectIdentity extends IDomainObject<Long> {

	void setId(Long id);
	
	AclSecuredClassDescription getObjectIdClass();

	void setObjectIdClass(AclSecuredClassDescription objectIdClass);

	AclSecuredObjectIdentity getParentObject();

	void setParentObject(AclSecuredObjectIdentity parentObject);

	Long getObjectIdIdentity();

	void setObjectIdIdentity(Long objectIdIdentity);

	AclSid getSid();

	void setSid(AclSid sid);

	boolean isEntriesInheriting();

	void setEntriesInheriting(boolean entriesInheriting);

	Serializable getIdentifier();

	Class<?> getJavaType();

}
