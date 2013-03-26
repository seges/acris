package sk.seges.acris.security.server.core.session.spring;

import javax.servlet.http.HttpSession;

import sk.seges.acris.security.server.core.request.api.HttpServletRequestProvider;
import sk.seges.acris.security.server.core.session.ServerSessionProvider;

public class SpringServerSessionProvider implements ServerSessionProvider {

	private HttpServletRequestProvider requestProvider;
	
	public SpringServerSessionProvider(HttpServletRequestProvider requestProvider) {
		this.requestProvider = requestProvider;
	}
	
	@Override
	public HttpSession getSession() {
		return requestProvider.getRequest().getSession();
	}

	@Override
	public HttpSession getSession(boolean create) {
		return requestProvider.getRequest().getSession(create);
	}
}