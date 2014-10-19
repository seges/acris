package sk.seges.acris.site.server.cache;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.constructs.web.filter.SimpleCachingHeadersPageCachingFilter;
import sk.seges.acris.security.server.util.LoginConstants;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;

public class CacheFilter extends SimpleCachingHeadersPageCachingFilter {

	@Override
	protected boolean filterNotDisabled(HttpServletRequest httpRequest) {
 		if (!httpRequest.getMethod().equals("GET")) {
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