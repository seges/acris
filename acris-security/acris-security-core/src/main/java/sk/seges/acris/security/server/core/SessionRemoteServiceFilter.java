package sk.seges.acris.security.server.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import sk.seges.acris.security.server.core.request.session.GWTRPCSessionHttpServletRequestWrapper;
import sk.seges.acris.security.server.core.request.session.SessionHttpServletRequestWrapper;

public class SessionRemoteServiceFilter implements Filter {
	private static final String REQUEST_WRAPPER = "requestWrapper";

	public static final String SESSION_ATTRIBUTE = "SESSION_ID";

	private Class<? extends SessionHttpServletRequestWrapper> requestWrapperClass = GWTRPCSessionHttpServletRequestWrapper.class;

	public SessionRemoteServiceFilter() {
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpSession currentSession = ((HttpServletRequest) request).getSession();
		if (currentSession != null) {
			SessionHandlerListener.accessFromContainer(currentSession.getId());
		}
		
		try {
			SessionHttpServletRequestWrapper requestWrapper = requestWrapperClass.getConstructor(HttpServletRequest.class).newInstance(request);
			chain.doFilter(requestWrapper, response);
		} catch (Exception e) {
			throw new ServletException(e);
		}
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@SuppressWarnings("unchecked")
	public void init(FilterConfig config) throws ServletException {
		String requestWrapperConfig = config.getInitParameter(REQUEST_WRAPPER);
		if(requestWrapperConfig != null && !requestWrapperConfig.isEmpty()) {
			try {
				this.requestWrapperClass = (Class<? extends SessionHttpServletRequestWrapper>) Class.forName(requestWrapperConfig);
			} catch (ClassNotFoundException e) {
				throw new ServletException(e);
			}
		}
	}
}