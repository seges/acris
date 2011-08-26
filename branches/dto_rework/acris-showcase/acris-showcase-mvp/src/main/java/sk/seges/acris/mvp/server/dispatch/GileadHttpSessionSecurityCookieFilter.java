package sk.seges.acris.mvp.server.dispatch;

import javax.servlet.http.HttpSession;

import org.gwtwidgets.server.spring.ServletUtils;

import com.gwtplatform.dispatch.server.AbstractHttpSessionSecurityCookieFilter;

public class GileadHttpSessionSecurityCookieFilter extends AbstractHttpSessionSecurityCookieFilter {

	public GileadHttpSessionSecurityCookieFilter(String securityCookieName) {
		super(securityCookieName);
	}

	@Override
	protected HttpSession getSession() {
		return ServletUtils.getRequest().getSession();
	}
}
