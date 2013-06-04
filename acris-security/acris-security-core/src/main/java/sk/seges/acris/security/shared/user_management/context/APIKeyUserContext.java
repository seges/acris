package sk.seges.acris.security.shared.user_management.context;

import sk.seges.acris.security.shared.user_management.domain.api.UserContext;

public class APIKeyUserContext implements UserContext {
	private static final long serialVersionUID = -4356765889005541780L;
	
	private String webId;
	private String apiKey;
	
	public APIKeyUserContext() { }
	
	public APIKeyUserContext(String webId, String apiKey) {
		this.webId = webId;
		this.apiKey = apiKey;
	}

	@Override
	public String getWebId() {
		return webId;
	}

	public void setWebId(String webId) {
		this.webId = webId;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
}
