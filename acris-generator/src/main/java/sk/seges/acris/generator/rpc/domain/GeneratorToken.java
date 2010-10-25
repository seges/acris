package sk.seges.acris.generator.rpc.domain;

import java.io.Serializable;

public class GeneratorToken implements Serializable {
	
	private static final long serialVersionUID = -1013144843773775841L;

	private String language;
	private String webId;
	private String niceUrl;

	public static final String DEFAULT_TOKEN = "";
	
	public GeneratorToken() {	
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

	public String getNiceUrl() {
		return niceUrl;
	}

	public void setNiceUrl(String niceUrl) {
		this.niceUrl = niceUrl;
	}
}