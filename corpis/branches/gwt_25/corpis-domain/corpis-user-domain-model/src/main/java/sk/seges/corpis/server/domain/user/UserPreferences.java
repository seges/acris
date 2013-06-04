package sk.seges.corpis.server.domain.user;

import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
interface UserPreferences extends IMutableDomainObject<Long> {

	String locale();
}