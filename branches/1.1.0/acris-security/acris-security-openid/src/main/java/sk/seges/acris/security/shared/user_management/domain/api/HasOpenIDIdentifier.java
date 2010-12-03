package sk.seges.acris.security.shared.user_management.domain.api;

import java.io.Serializable;

public interface HasOpenIDIdentifier extends Serializable {

	UserData getUser();

	void setUser(UserData user);

	String getOpenIDIdentifier();

	void setOpenIDIdentifier(String identifier);
}
