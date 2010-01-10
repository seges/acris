package sk.seges.acris.generator.rpc.domain;

import java.io.Serializable;

public class GeneratorToken implements Serializable {
	
	private static final long serialVersionUID = -1013144843773775841L;

	private String token;
	private String language;
	private String webId;

	public GeneratorToken() {	
	}
	
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getWebId() {
		return webId;
	}

	public void setWebId(String webId) {
		this.webId = webId;
	}
}
