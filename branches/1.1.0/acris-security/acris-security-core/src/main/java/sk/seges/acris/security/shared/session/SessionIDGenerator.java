/**
 * 
 */
package sk.seges.acris.security.shared.session;

import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

/**
 * Responsible for unique session ID generation.
 * 
 * @author ladislav.gazo
 */
public interface SessionIDGenerator {

	/**
	 * Generates session ID based (but not necessarily) on the login token.
	 * 
	 * @param token
	 * @return
	 */
	String generate(LoginToken token);
}
