package sk.seges.acris.security.server.core.request.api;

import javax.servlet.http.HttpServletRequest;

public interface HttpServletRequestProvider {

	HttpServletRequest getRequest();

}