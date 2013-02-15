/**
 * 
 */
package sk.seges.acris.security.shared.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import sk.seges.acris.security.shared.domain.ITransferableObject;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;

/**
 * @author eldzi
 */
public class ClientSession implements ITransferableObject {

	private static final long serialVersionUID = 1909822586051716898L;

	private static final String SESSION_ID_ATTRIBUTE = "sessionId";

	private Map<String, Comparable<? extends Serializable>> session;
	private Map<String, Serializable> clientSession;

	public Map<String, Comparable<? extends Serializable>> getSession() {
		if (session == null) {
			session = new HashMap<String, Comparable<? extends Serializable>>();
		}
		return session;
	}

	private Map<String, Serializable> getClientSession() {
		if (clientSession == null) {
			clientSession = new HashMap<String, Serializable>();
		}
		return clientSession;
	}
	
	public void setSession(Map<String, Comparable<? extends Serializable>> session) {
		this.session = session;
	}

	public String getSessionId() {
		return (String) getSession().get(SESSION_ID_ATTRIBUTE);
	}

	public void setSessionId(String sessionId) {
		getSession().put(SESSION_ID_ATTRIBUTE, sessionId);
	}

	private UserData<?> user;
	
	@SuppressWarnings("unchecked")
	public <T extends UserData<?>> T getUser() {
		return (T) user;
	}

	public <T extends UserData<?>> void setUser(T user) {
		this.user = user;
	}

	public void putSharedProperty(String key, Comparable<? extends Serializable> value) {
		getSession().put(key, value);
	}

	public void put(String key, Serializable value) {
		getClientSession().put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T extends Serializable> T get(String key) {
		if (session == null && clientSession == null) {
			return null;
		}
		if (session != null && session.containsKey(key)) {
			return (T) session.get(key);
		}
		if (clientSession != null && clientSession.containsKey(key)) {
			return (T) clientSession.get(key);
		}
		
		return null;
	}

	public ClientSession merge(ClientSession value) {
//		if (value.getUser() != null) {
//			this.setUser(value.getUser());
//		}
		for(Entry<String, Comparable<? extends Serializable>> entry : value.getSession().entrySet()) {
			if(getSession().containsKey(entry.getKey())) {
				continue;
			}
			
			session.put(entry.getKey(), entry.getValue());
		}
		//TODO sessionId also?
		return this;
	}
}