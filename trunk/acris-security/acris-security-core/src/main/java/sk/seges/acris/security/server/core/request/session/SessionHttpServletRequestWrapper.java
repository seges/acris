/**
 * 
 */
package sk.seges.acris.security.server.core.request.session;

import java.io.IOException;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpSession;

import sk.seges.acris.security.server.core.SessionHandlerListener;

/**
 * @author ladislav.gazo
 */
public abstract class SessionHttpServletRequestWrapper extends HttpServletRequestWrapper {
	protected  byte[] bytes;
	
	protected String sessionId;
	
	public SessionHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		try {
			extractSessionId(request);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	protected abstract void extractSessionId(HttpServletRequest request) throws Exception;
	
	@Override
	public HttpSession getSession() {
		HttpSession session = null;
		if(null != sessionId && !sessionId.isEmpty()) {
			session = SessionHandlerListener.getSession(sessionId);
		
			if (session != null) {
				return session;
			}
		}
		
		session = super.getSession();
		
		if (session != null) {
			String clientSessionId = sessionId;
			sessionId = session.getId();
			if(null == clientSessionId || clientSessionId.isEmpty()) {
				return session;
			}
			SessionHandlerListener.mapSessions(sessionId, clientSessionId);
		}
		
		return session;
	}
	
	@Override
	public HttpSession getSession(boolean create) {
		HttpSession session = null;
		if(null != sessionId && !sessionId.isEmpty()) {
			session = SessionHandlerListener.getSession(sessionId);
		
			if (session != null) {
				return session;
			}
		}

		session = super.getSession(create);
		
		if (session != null) {
			String clientSessionId = sessionId;
			sessionId = session.getId();
			if(null == clientSessionId || clientSessionId.isEmpty()) {
				return session;
			}
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
