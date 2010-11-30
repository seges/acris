package sk.seges.acris.generator.server.service;

import sk.seges.acris.generator.shared.domain.GeneratorToken;

public class GeneratorTokenWrapper extends GeneratorToken {

	private static final long serialVersionUID = 2314359284927886897L;

	private boolean isDefault = false;

	private GeneratorToken token;

	public GeneratorTokenWrapper(GeneratorToken token) {
		this.token = token;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public String getLanguage() {
		return token.getLanguage();
	}

	public void setLanguage(String language) {
		token.setLanguage(language);
	}

	public String getWebId() {
		return token.getWebId();
	}

	public void setWebId(String webId) {
		token.setWebId(webId);
	}

	public String getNiceUrl() {
		return token.getNiceUrl();
	}

	public void setNiceUrl(String niceUrl) {
		token.setNiceUrl(niceUrl);
	}
}