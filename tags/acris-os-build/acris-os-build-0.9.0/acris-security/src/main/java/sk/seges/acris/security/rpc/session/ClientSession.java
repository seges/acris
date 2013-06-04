/**
 * 
 */
package sk.seges.acris.security.rpc.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import sk.seges.acris.security.rpc.domain.ITransferableObject;
import sk.seges.acris.security.rpc.user_management.domain.GenericUser;

/**
 * @author eldzi
 */
public class ClientSession implements ITransferableObject {
	
	private static final long serialVersionUID = 1909822586051716898L;
	
	private static final String SESSION_ID_ATTRIBUTE = "sessionId";
	private static final String USER_ATTRIBUTE = "user";

	private Map<String, Serializable> session;
	

	public Map<String, Serializable> getSession() {
		if(session == null) {
			session = new HashMap<String, Serializable>();
		}
		return session;
	}
	
	public void setSession(Map<String, Serializable> session) {
		this.session = session;
	}

	public String getSessionId() {
		return (String)getSession().get(SESSION_ID_ATTRIBUTE);
	}

	public void setSessionId(String sessionId) {
		getSession().put(SESSION_ID_ATTRIBUTE, sessionId);
	}

	public GenericUser getUser() {
		return (GenericUser)getSession().get(USER_ATTRIBUTE);
	}

	public void setUser(GenericUser user) {
		getSession().put(USER_ATTRIBUTE, user);
	}
	
	public void put(String key, Serializable value) {
		getSession().put(key, value);
	}
	
	@SuppressWarnings("unchecked")
	public <T extends Serializable> T get(String key) {
		if(session == null) {
			return null;
		}
		return (T) session.get(key);
	}
}
