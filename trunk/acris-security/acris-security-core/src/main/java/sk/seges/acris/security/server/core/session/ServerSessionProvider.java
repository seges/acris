package sk.seges.acris.security.server.core.session;

import javax.servlet.http.HttpSession;

public interface ServerSessionProvider {

	HttpSession getSession();
	
	HttpSession getSession(boolean create);
}
