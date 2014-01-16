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

	private Map<String, PropertyHolder> session;
	private Map<String, Serializable> clientSession;

	public Map<String, PropertyHolder> getSession() {
		if (session == null) {
			session = new HashMap<String, PropertyHolder>();
		}
		return session;
	}

	private Map<String, Serializable> getClientSession() {
		if (clientSession == null) {
			clientSession = new HashMap<String, Serializable>();
		}
		return clientSession;
	}
	
	public void setSession(Map<String, PropertyHolder> session) {
		this.session = session;
	}

	protected <T> T getValue(String key) {
		PropertyHolder propertyHolder = getSession().get(key);
		if (propertyHolder == null) {
			return null;
		}
		return (T) propertyHolder.getValue();
	}

	public String getSessionId() {
		return getValue(SESSION_ID_ATTRIBUTE);
	}

	public void setSessionId(String sessionId) {
		getSession().put(SESSION_ID_ATTRIBUTE, new PropertyHolder(sessionId));
	}

	private T user;
	
	public T getUser() {
		return user;
	}

	public void setUser(T user) {
		this.user = user;
	}

	public void putSharedProperty(String key, String value) {
		getSession().put(key, new PropertyHolder(value, ValueType.STRING));
	}

	public void putSharedProperty(String key, Boolean value) {
		getSession().put(key, new PropertyHolder(value, ValueType.BOOLEAN));
	}

	public void putSharedProperty(String key, SessionArrayHolder value) {
		getSession().put(key, new PropertyHolder(value, ValueType.ARRAY));
	}

	public void putSharedProperty(String key, Enum<?> value) {
		getSession().put(key, new PropertyHolder(value, ValueType.ENUM));
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
			return getValue(key);
		}
		if (clientSession != null && clientSession.containsKey(key)) {
			return (S) clientSession.get(key);
		}
		
		return null;
	}

	public ClientSession<T> merge(ClientSession<T> value) {
		for(Entry<String, PropertyHolder> entry : value.getSession().entrySet()) {
			if(getSession().containsKey(entry.getKey())) {
				continue;
			}
			
			session.put(entry.getKey(), entry.getValue());
		}
		return this;
	}
	
	public void clear() {
		getClientSession().clear();
		getSession().clear();
	}
}