package sk.seges.acris.generator.server;


public class WebSettings {

	private String webId;
	private String lang;
	private String topLevelDomain;
	private String googleAnalyticsScript;
	
	public String getWebId() {
		return webId;
	}

	public void setWebId(String webId) {
		this.webId = webId;
	}

	public String getGoogleAnalyticsScript() {
		return googleAnalyticsScript;
	}

	public void setGoogleAnalyticsScript(String googleAnalyticsScript) {
		this.googleAnalyticsScript = googleAnalyticsScript;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getTopLevelDomain() {
		return topLevelDomain;
	}

	public void setTopLevelDomain(String topLevelDomain) {
		this.topLevelDomain = topLevelDomain;
	}
}