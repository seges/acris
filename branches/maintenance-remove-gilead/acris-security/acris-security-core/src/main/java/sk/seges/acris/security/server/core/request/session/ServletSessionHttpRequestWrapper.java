/**
 * 
 */
package sk.seges.acris.security.server.core.request.session;

import javax.servlet.http.HttpServletRequest;

/**
 * @author ladislav.gazo
 * 
 */
public class ServletSessionHttpRequestWrapper extends
		SessionHttpServletRequestWrapper {

	public ServletSessionHttpRequestWrapper(HttpServletRequest request) {
		super(request);
	}

	@Override
	protected void extractSessionId(HttpServletRequest request)
			throws Exception {
		sessionId = (String) request.getParameter("sessionid");
		if (sessionId == null) {
			sessionId = "";
		}
	}

}
