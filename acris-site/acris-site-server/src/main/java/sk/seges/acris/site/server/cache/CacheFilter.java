package sk.seges.acris.site.server.cache;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.constructs.web.filter.SimpleCachingHeadersPageCachingFilter;
import sk.seges.acris.security.server.util.LoginConstants;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

public class CacheFilter extends SimpleCachingHeadersPageCachingFilter {

	private static final String CODESVR = "gwt.codesvr=127.0.0.1:9998";
	@Override
	protected boolean filterNotDisabled(HttpServletRequest httpRequest) {
 		if (!httpRequest.getMethod().equals("GET") || (httpRequest.getHeader("referer") != null && httpRequest.getHeader("referer").contains(CODESVR))) {
 			return false;
 		}
 		HttpSession session = httpRequest.getSession();
 		if (session != null) {
 			LoginToken token = (LoginToken)session.getAttribute(LoginConstants.LOGIN_TOKEN_NAME);
 			if (token != null) {
 				if (token.isAdmin()) {
 					return false;
 				}
 			}
 		}
		return super.filterNotDisabled(httpRequest);
	}
 }