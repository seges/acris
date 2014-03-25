/**
 * 
 */
package sk.seges.acris.security.shared.session;

import sk.seges.acris.core.shared.model.IDataTransferObject;
import sk.seges.acris.security.shared.user_management.model.dto.GenericUserDTO;
import sk.seges.sesam.shared.model.api.PropertyHolder;
import sk.seges.sesam.shared.model.api.SessionArrayHolder;
import sk.seges.sesam.shared.model.api.TransferableEnum;
import sk.seges.sesam.shared.model.api.ValueType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author eldzi
 */
public class ClientSessionDTO implements IDataTransferObject {

	private static final long serialVersionUID = 1909822586051716898L;

	private static final String SESSION_ID_ATTRIBUTE = "sessionId";

	private Map<String, PropertyHolder> session;
	private final Map<String, Serializable> clientSession = new HashMap<String, Serializable>();

	public Map<String, PropertyHolder> getSession() {
		if (session == null) {
			session = new HashMap<String, PropertyHolder>();
		}
		return session;
	}

	private Map<String, Serializable> getClientSession() {
		return clientSession;
	}
	
	public void setSession(Map<String, PropertyHolder> session) {
		this.session = session;
	}

	protected <S> S getValue(String key) {
		PropertyHolder propertyHolder = getSession().get(key);
		if (propertyHolder == null) {
			return null;
		}
		return (S) propertyHolder.getValue();
	}

	public String getSessionId() {
		return getValue(SESSION_ID_ATTRIBUTE);
	}

	public void setSessionId(String sessionId) {
		getSession().put(SESSION_ID_ATTRIBUTE, new PropertyHolder(sessionId));
	}

	private GenericUserDTO user;
	
	public GenericUserDTO getUser() {
		return user;
	}

	public void setUser (GenericUserDTO user) {
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

	public void putSharedProperty(String key, TransferableEnum value) {
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

	public ClientSessionDTO merge(ClientSessionDTO value) {
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