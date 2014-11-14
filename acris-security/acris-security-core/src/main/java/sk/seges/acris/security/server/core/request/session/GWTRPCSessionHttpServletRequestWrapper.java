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

	private String webId;
	private static final int SPLIT_LIMIT = 4;

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
			
			@Override
			public int getSessionDelimiterIndex(String payload) {
				int index = payload.indexOf(RequestWrapperConstants.SESSION_DELIMITER);
				if (index >= 0) {
					return index;
				}
				return payload.indexOf(RequestWrapperConstants.CHROME_SESSION_DELIMITER);
			}
				
			@Override
			public String getSessionId(String payload) {
				String sessionId = "";
				String[] splitedParams = payload.split(String.valueOf(RequestWrapperConstants.SESSION_DELIMITER), SPLIT_LIMIT);
				if(splitedParams.length == 0) {
					splitedParams = payload.split(String.valueOf(RequestWrapperConstants.CHROME_SESSION_DELIMITER), SPLIT_LIMIT);
				}
				if(splitedParams.length == 4){
					sessionId = splitedParams[2];
				}		
				return sessionId;
			}

			@Override
			public int getSessionDelimiterIndex(String payload, int index) {
				int i = payload.indexOf(RequestWrapperConstants.SESSION_DELIMITER, index + getSessionDelimiterLength());

				if (i >= 0) {
					return i;
				}

				return payload.indexOf(RequestWrapperConstants.CHROME_SESSION_DELIMITER, index + getSessionDelimiterLength());
			}
			
			@Override
			public String getWebId(String payload) {
				String webId = "";
				String[] splitedParams = payload.split(String.valueOf(RequestWrapperConstants.SESSION_DELIMITER), SPLIT_LIMIT);
				if(splitedParams.length == 0) {
					splitedParams = payload.split(String.valueOf(RequestWrapperConstants.CHROME_SESSION_DELIMITER), SPLIT_LIMIT);
				}
				if(splitedParams.length == 4){
					webId = splitedParams[1];
				}
				return webId;
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

			@Override
			public int getSessionDelimiterIndex(String payload) {
				return payload.indexOf(RequestWrapperConstants.SESSION_DELIMITER);
			}
			
			@Override
			public String getSessionId(String payload) {
				String sessionId = "";
				String[] splitedParams = payload.split(String.valueOf(RequestWrapperConstants.SESSION_DELIMITER),
						SPLIT_LIMIT);
				if (splitedParams.length == 4) {
					sessionId = splitedParams[2];
				}
				return sessionId;
			}
			
			@Override
			public int getSessionDelimiterIndex(String payload, int index) {
				return payload.indexOf(RequestWrapperConstants.SESSION_DELIMITER, index + getSessionDelimiterLength());
			}

			@Override
			public String getWebId(String payload) {
				String webId = "";
				String[] splitedParams = payload.split(String.valueOf(RequestWrapperConstants.SESSION_DELIMITER), SPLIT_LIMIT);
				if(splitedParams.length == 4){
					webId = splitedParams[1];
				}		
				return webId;
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
		public abstract String getSessionId(String payload);
		public abstract String getWebId(String payload);
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

			int sessionDelimiterIndex = handler.getSessionDelimiterIndex(payload);

			if (sessionDelimiterIndex == 0) {
				webId = handler.getWebId(payload);
				sessionId = handler.getSessionId(payload);
				if(webId != null && webId.length() > 0){
					sessionDelimiterIndex = payload.indexOf(webId) + webId.length();
				} else {
					sessionDelimiterIndex++;
				}
				sessionDelimiterIndex = handler.getSessionDelimiterIndex(payload, sessionDelimiterIndex);
				SessionHandlerListener.accessManually(sessionId);
				payload = payload.substring(sessionDelimiterIndex + handler.getSessionDelimiterLength());
			} else {
				webId = "";
				sessionId = "";				
			}
			
			if (handler.equals(RequestMethodHandler.GET)) {
				queryString = payload;
			} else {
				bytes = payload.getBytes(encoding);
			}
		}
	}

	public String getWebId() {
		return webId;
	}

}
