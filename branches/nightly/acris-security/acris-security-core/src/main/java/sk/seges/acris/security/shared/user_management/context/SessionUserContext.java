package sk.seges.acris.security.shared.user_management.context;

import sk.seges.acris.security.shared.user_management.domain.api.UserContext;

public class SessionUserContext implements UserContext{
	private static final long serialVersionUID = -1631599046237774825L;

	private String webId;

	public SessionUserContext() { }
	
	public SessionUserContext(String webId) {
		this.webId = webId;
	}

	public String getWebId() {
		return webId;
	}

	public void setWebId(String webId) {
		this.webId = webId;
	}
}
