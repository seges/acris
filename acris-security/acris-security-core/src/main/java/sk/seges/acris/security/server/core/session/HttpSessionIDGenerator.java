/**
 * 
 */
package sk.seges.acris.security.server.core.session;

import sk.seges.acris.security.server.session.SessionIDGenerator;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

/**
 * Retrieves a session ID from HTTP request session. If session doesn't exist it
 * will be created.
 * 
 * @author ladislav.gazo
 */
public class HttpSessionIDGenerator implements SessionIDGenerator {

	private ServerSessionProvider sessionProvider;

	public HttpSessionIDGenerator(ServerSessionProvider sessionProvider) {
		this.sessionProvider = sessionProvider;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * sk.seges.acris.security.rpc.session.SessionIDGenerator#generate(sk.seges
	 * .acris.security.rpc.user_management.domain.LoginToken)
	 */
	@Override
	public String generate(LoginToken token) {
		return sessionProvider.getSession(true).getId();
	}
}
