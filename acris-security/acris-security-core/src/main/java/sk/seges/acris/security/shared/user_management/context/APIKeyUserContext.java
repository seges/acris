package sk.seges.acris.security.shared.user_management.context;


public class APIKeyUserContext extends CommonUserContext {
	private static final long serialVersionUID = -4356765889005541780L;
	
	private String apiKey;
	
	public APIKeyUserContext() {}
	
	public APIKeyUserContext(String webId, String apiKey, String locale) {
		super(webId, locale);
		this.apiKey = apiKey;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}
}
