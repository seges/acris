package sk.seges.acris.site.server.cache;


import java.io.IOException;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import sk.seges.acris.security.server.util.LoginConstants;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

public class CacheFilter implements Filter {

 	private FilterConfig filterConfig;
 	private int MINUTES_CACHE = 5;

 	public void doFilter( ServletRequest request, ServletResponse response, FilterChain filterChain) 
 			throws IOException, ServletException {
 		HttpServletRequest httpRequest = (HttpServletRequest)request;
 		
 		if (!disableCaching(httpRequest)) {
 			String requestURI = httpRequest.getRequestURI();
	 		if( !requestURI.contains(".nocache.") ){
	 			long today = new Date().getTime();
	 			HttpServletResponse httpResponse = (HttpServletResponse)response;
	 			httpResponse.setDateHeader("Expires", today + (MINUTES_CACHE*60*1000));
	 		}
 		}
 		filterChain.doFilter(request, response);
 	}

 	public void init(FilterConfig filterConfig) throws ServletException {
 		this.filterConfig = filterConfig;
 	}

 	public void destroy() {
 		this.filterConfig = null;
 	}
 	
 	private boolean disableCaching(HttpServletRequest httpRequest) {
 		HttpSession session = httpRequest.getSession();
 		boolean result = false;
 		if (session != null) {
 			LoginToken token = (LoginToken)session.getAttribute(LoginConstants.LOGIN_TOKEN_NAME);
 			if (token != null) {
 				result = token.isAdmin();
 			}
 		}
 		return result;
 	}
 }