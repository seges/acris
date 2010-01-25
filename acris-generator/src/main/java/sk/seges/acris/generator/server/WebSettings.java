package sk.seges.acris.generator.server;

public class WebSettings {

	private String webId;
	private String language;

	private String topLevelDomain; // e.g. http://www.seges.sk

	public String getWebId() {
		return webId;
	}

	public void setWebId(String webId) {
		this.webId = webId;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String getTopLevelDomain() {
		return topLevelDomain;
	}

	public void setTopLevelDomain(String topLevelDomain) {
		this.topLevelDomain = topLevelDomain;
	}
}