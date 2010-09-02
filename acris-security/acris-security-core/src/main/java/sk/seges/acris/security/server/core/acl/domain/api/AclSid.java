package sk.seges.acris.security.server.core.acl.domain.api;

import sk.seges.acris.binding.client.annotations.BeanWrapper;
import sk.seges.acris.binding.client.processor.DBPropertyConverter;
import sk.seges.acris.binding.client.processor.POJOPropertyConverter;
import sk.seges.sesam.domain.IDomainObject;

@BeanWrapper(beanPropertyConverter = {POJOPropertyConverter.class, DBPropertyConverter.class})
public interface AclSid extends IDomainObject<Long> {

	boolean isPrincipal();

	void setPrincipal(boolean principal);

	String getSid();

	void setSid(String sid);

}