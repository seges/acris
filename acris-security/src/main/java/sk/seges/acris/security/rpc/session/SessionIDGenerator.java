/**
 * 
 */
package sk.seges.acris.security.rpc.session;

import sk.seges.acris.security.rpc.user_management.domain.LoginToken;

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
