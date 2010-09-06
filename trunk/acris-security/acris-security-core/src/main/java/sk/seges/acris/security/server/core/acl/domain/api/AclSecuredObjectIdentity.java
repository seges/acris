package sk.seges.acris.security.server.core.acl.domain.api;

import java.io.Serializable;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.acris.binding.client.processor.DBPropertyConverter;
import sk.seges.acris.binding.client.processor.PojoPropertyConverter;
import sk.seges.sesam.domain.IDomainObject;

@BeanWrapper(beanPropertyConverter = {PojoPropertyConverter.class, DBPropertyConverter.class})
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
