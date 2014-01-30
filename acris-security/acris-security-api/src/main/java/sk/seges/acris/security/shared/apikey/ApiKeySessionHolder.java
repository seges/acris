package sk.seges.acris.security.shared.apikey;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashMap;

public class ApiKeySessionHolder implements Serializable {
	private static final long serialVersionUID = 1862533361300701448L;

	private int EXPIRATION_LIMIT = 30000;
	
	private static LinkedHashMap<String, ApiKeySession> sessions;
	
	public ApiKeySessionHolder() {
		sessions = new LinkedHashMap<String, ApiKeySession>();
	}
	
	public void appendSesion(String apiKey, Long userId) {
		ApiKeySession session = new ApiKeySession(userId, new Date().getTime() + EXPIRATION_LIMIT);
		sessions.put(apiKey, session);
	}
	
	public ApiKeySession getSession(String apiKey) {
		if (sessions.isEmpty()) {
			return null;
		}

		ApiKeySession session = sessions.get(apiKey);
		Long now = new Date().getTime();
		if (session != null && ((now - EXPIRATION_LIMIT) < session.getExpirationTimestamp())) {
			return session;
		}
		return null;
	}
}
