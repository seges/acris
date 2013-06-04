package sk.seges.acris.security.server.utils;

import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.user_management.server.model.data.UserData;

public interface TokenConverter {
	
	/**
	 * Converts source token and user data to a new token.
	 * @param token
	 * @param user
	 * @return
	 */
	LoginToken convert(LoginToken source, UserData user);
}
