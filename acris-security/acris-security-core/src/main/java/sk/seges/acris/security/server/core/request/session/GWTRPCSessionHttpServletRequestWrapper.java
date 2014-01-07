/**
 * 
 */
package sk.seges.acris.security.server.core.request.session;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import sk.seges.acris.security.server.core.SessionHandlerListener;

import com.google.gwt.user.server.rpc.RPCServletUtils;

/**
 * @author ladislav.gazo
 */
public class GWTRPCSessionHttpServletRequestWrapper extends SessionHttpServletRequestWrapper {

	public GWTRPCSessionHttpServletRequestWrapper(HttpServletRequest request)
			throws IOException, ServletException {
		super(request);
	}

	@Override
	protected void extractSessionId(HttpServletRequest request) throws Exception {
		String contentType = request.getContentType();
		if (contentType != null && contentType.indexOf("text/x-gwt-rpc") >= 0) {
			//get and remove sessionId from http request
			final String encoding = request.getCharacterEncoding();

			String payload = null;
			boolean isGetMethod = request.getMethod() != null && request.getMethod().toLowerCase().equals("get");

			if (isGetMethod) {
				payload = request.getQueryString();
			} else {
				payload = RPCServletUtils.readContentAsUtf8((HttpServletRequest) this.getRequest(), true);
			}

			int index = payload.indexOf('\uffff');
			if (index == 0) {
				index = payload.indexOf('\uffff', index + 1);
				sessionId = payload.substring(1, index);
				SessionHandlerListener.accessManually(sessionId);
				payload = payload.substring(index + 1);

				if (isGetMethod) {
					queryString = payload;
				} else {
					bytes = payload.getBytes(encoding);
				}
			} else {
				sessionId = "";
				if (isGetMethod) {
					queryString = payload;
				} else {
					bytes = payload.getBytes(encoding);
				}
			}
		}
	}

}
