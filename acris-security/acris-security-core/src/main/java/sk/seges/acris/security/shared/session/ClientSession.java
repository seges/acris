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
	private static final String USER_ATTRIBUTE = "user";

	private Map<String, Serializable> session;

	public Map<String, Serializable> getSession() {
		if (session == null) {
			session = new HashMap<String, Serializable>();
		}
		return session;
	}

	public void setSession(Map<String, Serializable> session) {
		this.session = session;
	}

	public String getSessionId() {
		return (String) getSession().get(SESSION_ID_ATTRIBUTE);
	}

	public void setSessionId(String sessionId) {
		getSession().put(SESSION_ID_ATTRIBUTE, sessionId);
	}

	@SuppressWarnings("unchecked")
	public <T extends UserData<?>> T getUser() {
		return (T) getSession().get(USER_ATTRIBUTE);
	}

	public <T extends UserData<?>> void setUser(T user) {
		getSession().put(USER_ATTRIBUTE, user);
	}

	public void put(String key, Serializable value) {
		getSession().put(key, value);
	}

	@SuppressWarnings("unchecked")
	public <T extends Serializable> T get(String key) {
		if (session == null) {
			return null;
		}
		return (T) session.get(key);
	}

	public ClientSession merge(ClientSession value) {
//		if (value.getUser() != null) {
//			this.setUser(value.getUser());
//		}
		for(Entry<String, Serializable> entry : value.getSession().entrySet()) {
			if(session.containsKey(entry.getKey())) {
				continue;
			}
			
			session.put(entry.getKey(), entry.getValue());
		}
		//TODO sessionId also?
		return this;
	}
}