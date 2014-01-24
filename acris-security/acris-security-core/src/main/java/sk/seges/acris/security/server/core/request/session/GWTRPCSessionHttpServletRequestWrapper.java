/**
 * 
 */
package sk.seges.acris.security.server.core.request.session;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

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

	private static final char SESSION_DELIMITER = '\uffff';
	private static final char CHROME_SESSION_DELIMITER = '\ufffd';

	public enum RequestMethodHandler {
		GET {
			@Override
			String getMethodName() {
				return "get";
			}

			@Override
			public int getSessionDelimiterLength() {
				return 1;
			}

			public int getSessionDelimiterIndex(String payload) {
				int index = payload.indexOf(SESSION_DELIMITER);
				if (index >= 0) {
					return index;
				}

				return payload.indexOf(CHROME_SESSION_DELIMITER);
			}

			public int getSessionDelimiterIndex(String payload, int index) {
				int i = payload.indexOf(SESSION_DELIMITER, index + getSessionDelimiterLength());

				if (i >= 0) {
					return i;
				}

				return payload.indexOf(CHROME_SESSION_DELIMITER, index + getSessionDelimiterLength());
			}

			@Override
			public String getPayload(HttpServletRequest request) {
				try {
					return URLDecoder.decode(request.getQueryString(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					throw new RuntimeException(e);
				}
			}
		},
		POST {
			@Override
			String getMethodName() {
				return "post";
			}

			@Override
			public int getSessionDelimiterLength() {
				return 1;
			}

			public int getSessionDelimiterIndex(String payload) {
				return payload.indexOf(SESSION_DELIMITER);
			}

			public int getSessionDelimiterIndex(String payload, int index) {
				return payload.indexOf(SESSION_DELIMITER, index + getSessionDelimiterLength());
			}

			@Override
			public String getPayload(HttpServletRequest request) {
				try {
					return RPCServletUtils.readContentAsUtf8(request, true);
				} catch (IOException e) {
					throw new RuntimeException(e);
				} catch (ServletException e) {
					throw new RuntimeException(e);
				}
			}
		};

		abstract String getMethodName();
		public abstract int getSessionDelimiterLength();
		public abstract int getSessionDelimiterIndex(String payload);
		public abstract int getSessionDelimiterIndex(String payload, int index);
		public abstract String getPayload(HttpServletRequest request);

		public static RequestMethodHandler getHandler(HttpServletRequest request) {
			if (request.getMethod() == null) {
				return RequestMethodHandler.POST;
			}

			for (RequestMethodHandler handler: RequestMethodHandler.values()) {
				if (request.getMethod().toLowerCase().equals(handler.getMethodName())) {
					return handler;
				}
			}

			throw new RuntimeException("Unsupported HTTP request method type!");
		}
	}

	@Override
	protected void extractSessionId(HttpServletRequest request) throws Exception {
		String contentType = request.getContentType();
		if (contentType != null && contentType.indexOf("text/x-gwt-rpc") >= 0) {
			//get and remove sessionId from http request
			final String encoding = request.getCharacterEncoding();

			RequestMethodHandler handler = RequestMethodHandler.getHandler(request);

			String payload = handler.getPayload(request);

			int index = handler.getSessionDelimiterIndex(payload);

			if (index == 0) {
				index = handler.getSessionDelimiterIndex(payload, index);
				sessionId = payload.substring(handler.getSessionDelimiterLength(), index);
				SessionHandlerListener.accessManually(sessionId);
				payload = payload.substring(index + handler.getSessionDelimiterLength());

				if (handler.equals(RequestMethodHandler.GET)) {
					queryString = payload;
				} else {
					bytes = payload.getBytes(encoding);
				}
			} else {
				sessionId = "";
				if (handler.equals(RequestMethodHandler.GET)) {
					queryString = payload;
				} else {
					bytes = payload.getBytes(encoding);
				}
			}
		}
	}

}
