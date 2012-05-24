package sk.seges.acris.domain.shared.domain.base;

import sk.seges.acris.domain.shared.domain.api.ContentPkData;

public class ContentPkBase implements ContentPkData {

	private static final long serialVersionUID = -7464299695597839783L;

	private Long id;

	private String webId;

	private String language;

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public String getWebId() {
		return webId;
	}

	@Override
	public void setWebId(String webId) {
		this.webId = webId;
	}

	@Override
	public String getLanguage() {
		return language;
	}

	@Override
	public void setLanguage(String language) {
		this.language = language;
	}
}
