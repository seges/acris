package sk.seges.acris.security.acl;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IDomainObject;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
interface AclSid extends IMutableDomainObject<Long> {

	boolean principal();

	String sid();

}