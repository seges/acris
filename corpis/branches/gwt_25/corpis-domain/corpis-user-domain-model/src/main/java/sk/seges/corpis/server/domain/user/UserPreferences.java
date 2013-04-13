package sk.seges.corpis.server.domain.user;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IDomainObject;

@DomainInterface
@BaseObject
interface UserPreferences extends IDomainObject<Long> {

	String locale();
}