package sk.seges.acris.security.acl;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
interface AclSecuredClassDescription extends IDomainObject<Long> {

	String className();
}