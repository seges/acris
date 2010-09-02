package sk.seges.acris.security.server.core.acl.domain.api;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.acris.binding.client.processor.DBPropertyConverter;
import sk.seges.acris.binding.client.processor.POJOPropertyConverter;
import sk.seges.sesam.domain.IDomainObject;

@BeanWrapper(beanPropertyConverter = {POJOPropertyConverter.class, DBPropertyConverter.class})
public interface AclSecuredClassDescription extends IDomainObject<Long> {

	String getClassName();

	void setClassName(String className);
}
