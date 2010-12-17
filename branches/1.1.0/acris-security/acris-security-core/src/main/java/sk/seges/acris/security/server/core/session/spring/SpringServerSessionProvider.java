package sk.seges.acris.security.server.core.session.spring;

import javax.servlet.http.HttpSession;

import org.gwtwidgets.server.spring.ServletUtils;

import sk.seges.acris.security.server.core.session.ServerSessionProvider;

public class SpringServerSessionProvider implements ServerSessionProvider {

	@Override
	public HttpSession getSession() {
		return ServletUtils.getRequest().getSession();
	}

	@Override
	public HttpSession getSession(boolean create) {
		return ServletUtils.getRequest().getSession(create);
	}
}
