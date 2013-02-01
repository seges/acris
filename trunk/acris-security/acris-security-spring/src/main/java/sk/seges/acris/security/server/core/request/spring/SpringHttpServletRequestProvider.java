package sk.seges.acris.security.server.core.request.spring;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import sk.seges.acris.security.server.core.request.api.HttpServletRequestProvider;

public class SpringHttpServletRequestProvider implements HttpServletRequestProvider {

	@Override
	public HttpServletRequest getRequest() {
		return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
	}

}