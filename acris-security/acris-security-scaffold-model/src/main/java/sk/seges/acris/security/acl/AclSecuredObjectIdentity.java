package sk.seges.acris.security.acl;

import java.io.Serializable;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IMutableDomainObject;
import sk.seges.sesam.pap.model.annotation.ReadOnly;

@DomainInterface
@BaseObject
interface AclSecuredObjectIdentity extends IMutableDomainObject<Long> {

	AclSecuredClassDescription objectIdClass();

	AclSecuredObjectIdentity parentObject();

	Long objectIdIdentity();
	
	AclSid sid();
	
	boolean entriesInheriting();

	@ReadOnly
	Serializable identifier();

	@ReadOnly
	Class<?> javaType();
}