package sk.seges.acris.generator.server.domain;

import sk.seges.acris.generator.server.domain.api.PersistentDataProvider;

public class TokenPersistentDataProvider implements PersistentDataProvider {

	private static final long serialVersionUID = -5150451260602100816L;

	private String id;
	private String content;
	private String webId;
	private String alias;

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void setId(String t) {
		this.id = t;
	}

	@Override
	public String getContent() {
		return content;
	}

	@Override
	public void setContent(String content) {
		this.content = content;
	}

	@Override
	public String getWebId() {
		return webId;
	}

	@Override
	public void setWebId(String webId) {
		this.webId = webId;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
}