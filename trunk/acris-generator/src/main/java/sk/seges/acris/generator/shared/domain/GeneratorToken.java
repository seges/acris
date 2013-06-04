package sk.seges.acris.generator.shared.domain;

import java.io.Serializable;

public class GeneratorToken implements Serializable {
	
	private static final long serialVersionUID = -1013144843773775841L;

	private String language;
	private String webId;
	private String alias;
	private String niceUrl;
	private boolean defaultToken;

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
	
	public boolean isDefaultToken() {
		return defaultToken;
	}
	
	public void setDefaultToken(boolean defaultToken) {
		this.defaultToken = defaultToken;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((alias == null) ? 0 : alias.hashCode());
		result = prime * result + (defaultToken ? 1231 : 1237);
		result = prime * result + ((language == null) ? 0 : language.hashCode());
		result = prime * result + ((niceUrl == null) ? 0 : niceUrl.hashCode());
		result = prime * result + ((webId == null) ? 0 : webId.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GeneratorToken other = (GeneratorToken) obj;
		if (alias == null) {
			if (other.alias != null)
				return false;
		} else if (!alias.equals(other.alias))
			return false;
		if (defaultToken != other.defaultToken)
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (niceUrl == null) {
			if (other.niceUrl != null)
				return false;
		} else if (!niceUrl.equals(other.niceUrl))
			return false;
		if (webId == null) {
			if (other.webId != null)
				return false;
		} else if (!webId.equals(other.webId))
			return false;
		return true;
	}
}