package sk.seges.sesam.security.server.provider.acl;

import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.security.server.model.acl.AclSecurityData;

public interface AclSecurityDataProvider<T extends IDomainObject<?>> {

	AclSecurityData getSecurityData(T t);

}