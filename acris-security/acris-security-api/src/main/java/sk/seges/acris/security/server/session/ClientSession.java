/**
 * 
 */
package sk.seges.acris.security.server.session;

import sk.seges.corpis.server.domain.user.server.model.data.UserData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author eldzi
 */
public class ClientSession {

	private Map<String, Object> session;
	private static final String SESSION_ID_ATTRIBUTE = "sessionId";

	public Map<String, Object> getSession() {
		if (session == null) {
			session = new HashMap<String, Object>();
		}
		return session;
	}

	public void setSession(Map<String, Object> session) {
		this.session = session;
	}

	public String getSessionId() {
		return get(SESSION_ID_ATTRIBUTE);
	}

	public void setSessionId(String sessionId) {
		put(SESSION_ID_ATTRIBUTE, sessionId);
	}

	private UserData user;
	
	public UserData getUser() {
		return user;
	}

	public void setUser(UserData user) {
		this.user = user;
	}

	@SuppressWarnings("unchecked")
	public <S> S get(String key) {
		return (S) getSession().get(key);
	}

	public void put(String key, Object value) {
		getSession().put(key, value);
	}

	public void clear() {
		getSession().clear();
	}
}