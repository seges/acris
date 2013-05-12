package sk.seges.acris.security.server.core.request.gilead;

import javax.servlet.http.HttpServletRequest;

import org.gwtwidgets.server.spring.ServletUtils;

import sk.seges.acris.security.server.core.request.api.HttpServletRequestProvider;

public class GileadHttpServletRequestProvider implements HttpServletRequestProvider {

	@Override
	public HttpServletRequest getRequest() {
		return ServletUtils.getRequest();
	}
}