package sk.seges.acris.security.server.core;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;


import com.google.gwt.user.server.rpc.RPCServletUtils;

public class SessionRemoteServiceFilter implements Filter {
	public static final String SESSION_ATTRIBUTE = "SESSION_ID";
	
	class SessionHttpServletRequestWrapper extends HttpServletRequestWrapper {

		private byte[] bytes;
		
		private String sessionId;
		
		public SessionHttpServletRequestWrapper(HttpServletRequest request) throws IOException, ServletException {
			super(request);
			String contentType = request.getContentType();
			if (contentType != null && contentType.indexOf("text/x-gwt-rpc") >= 0) {
				//get and remove sessionId from http request
				final String encoding = request.getCharacterEncoding();
				String payload = RPCServletUtils.readContentAsUtf8(
						(HttpServletRequest) this.getRequest(), true);
				int index = payload.indexOf('\uffff');
				if (index == 0) {
					index = payload.indexOf('\uffff', index + 1);
					sessionId = payload.substring(1, index);
					SessionHandlerListener.accessManually(sessionId);
					payload = payload.substring(index + 1);
					bytes = payload.getBytes(encoding);
				} else {
					sessionId = "";
					bytes = payload.getBytes(encoding);
				}
			}
		}

		@Override
		public HttpSession getSession() {
			HttpSession session = SessionHandlerListener.getSession(sessionId);
			
			if (session != null) {
				return session;
			}
			
			session = super.getSession();
			
			if (session != null) {
				String clientSessionId = sessionId;
				sessionId = session.getId();
				SessionHandlerListener.mapSessions(sessionId, clientSessionId);
			}
			
			return session;
		}
		
		@Override
		public HttpSession getSession(boolean create) {
			HttpSession session = SessionHandlerListener.getSession(sessionId);
			
			if (session != null) {
				return session;
			}

			session = super.getSession(create);
			
			if (session != null) {
				String clientSessionId = sessionId;
				sessionId = session.getId();
				SessionHandlerListener.mapSessions(sessionId, clientSessionId);
			}
			
			return session;
		}
		
		@Override
		public int getContentLength() {
			if (bytes == null) {
				return super.getContentLength();
			}
			return bytes.length;
		}
		
		@Override
		public ServletInputStream getInputStream() throws IOException {
			if (bytes == null) {
				return super.getInputStream();
			}
			return new ServletInputStream() {
				int index = 0;
				public int read() throws IOException {
					if (index >= bytes.length) {
						return -1;
					}
					return bytes[index++];
				}
				
			};
		}
	}

	public SessionRemoteServiceFilter() {
	}
	
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpSession currentSession = ((HttpServletRequest) request).getSession();
		if (currentSession != null) {
			SessionHandlerListener.accessFromContainer(currentSession.getId());
		}
		final SessionHttpServletRequestWrapper shsrw = new SessionHttpServletRequestWrapper((HttpServletRequest) request);
		//super.doFilter(shsrw, response, chain);
		chain.doFilter(shsrw, response);
	}

	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		
	}
}