/**
 * 
 */
package sk.seges.acris.security.shared.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import sk.seges.acris.core.client.rpc.IDataTransferObject;

/**
 * @author eldzi
 */
public class ClientSession<T> implements IDataTransferObject {

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

	private T user;
	
	public T getUser() {
		return user;
	}

	public void setUser(T user) {
		this.user = user;
	}

	public void putSharedProperty(String key, Comparable<? extends Serializable> value) {
		getSession().put(key, value);
	}

	public void put(String key, Serializable value) {
		getClientSession().put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <S> S get(String key) {
		if (session == null && clientSession == null) {
			return null;
		}
		if (session != null && session.containsKey(key)) {
			return (S) session.get(key);
		}
		if (clientSession != null && clientSession.containsKey(key)) {
			return (S) clientSession.get(key);
		}
		
		return null;
	}

	public ClientSession<T> merge(ClientSession<T> value) {
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