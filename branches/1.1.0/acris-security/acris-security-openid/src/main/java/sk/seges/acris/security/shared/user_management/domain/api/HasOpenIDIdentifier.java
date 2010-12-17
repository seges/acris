package sk.seges.acris.security.shared.user_management.domain.api;

import sk.seges.sesam.domain.IMutableDomainObject;

public interface HasOpenIDIdentifier extends IMutableDomainObject<String> {

	UserData<?> getUser();

	void setUser(UserData<?> user);
}
