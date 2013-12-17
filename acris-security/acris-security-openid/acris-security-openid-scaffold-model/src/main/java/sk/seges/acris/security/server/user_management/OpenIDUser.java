package sk.seges.acris.security.server.user_management;

import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;
import sk.seges.corpis.appscaffold.shared.annotation.BaseObject;
import sk.seges.corpis.appscaffold.shared.annotation.DomainInterface;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;
import sk.seges.sesam.domain.IMutableDomainObject;

@DomainInterface
@BaseObject
public interface OpenIDUser extends IMutableDomainObject<String> {

	UserData user();

	String email();

	OpenIDProvider provider();

	String locale();
}