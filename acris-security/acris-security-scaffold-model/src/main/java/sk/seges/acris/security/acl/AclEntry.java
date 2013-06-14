package sk.seges.acris.security.acl;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
interface AclEntry extends IMutableDomainObject<Long> {

	AclSecuredObjectIdentity objectIdentity();

	AclSid sid();

	int mask();

	int aceOrder();

	boolean granting();

	boolean auditSuccess();

	boolean auditFailure();
}