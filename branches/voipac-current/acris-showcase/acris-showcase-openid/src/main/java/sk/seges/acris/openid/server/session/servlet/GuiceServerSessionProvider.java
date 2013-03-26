package sk.seges.acris.openid.server.session.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import sk.seges.acris.security.server.core.session.ServerSessionProvider;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class GuiceServerSessionProvider implements ServerSessionProvider {

	private static final long serialVersionUID = 9109221077910781993L;

	private Provider<HttpServletRequest> requestProvider;

	@Inject
	public GuiceServerSessionProvider(Provider<HttpServletRequest> requestProvider) {
		this.requestProvider = requestProvider;
	}

	@Override
	public HttpSession getSession() {
		return requestProvider.get().getSession();
	}

	@Override
	public HttpSession getSession(boolean create) {
		return requestProvider.get().getSession(create);
	}
}
