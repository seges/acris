package sk.seges.acris.generator.rpc.domain;

public class GeneratorToken {
	
	private String niceUrl;
	private String token;
	private String language;
	private String webId;

	public String getNiceUrl() {
		return niceUrl;
	}

	public void setNiceUrl(String niceUrl) {
		this.niceUrl = niceUrl;
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
