package sk.seges.acris.generator.server.processor;

import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.acris.showcase.mora.client.configuration.NameTokens;


public class MoviesContent implements ContentData<Long> {

	private static final long serialVersionUID = -2924442239602100352L;

	@Override
	public void setId(Long t) {
	}

	@Override
	public Long getId() {
		return 0L;
	}

	@Override
	public String getWebId() {
		return "test";
	}

	@Override
	public void setWebId(String webId) {
	}

	@Override
	public String getKeywords() {
		return "movies";
	}

	@Override
	public void setKeywords(String keywords) {
	}

	@Override
	public String getDescription() {
		return "description";
	}

	@Override
	public void setDescription(String description) {
	}

	@Override
	public String getTitle() {
		return "title";
	}

	@Override
	public void setTitle(String title) {
	}

	@Override
	public String getNiceUrl() {
		return NameTokens.HOME_PAGE;
	}

	@Override
	public void setNiceUrl(String niceUlr) {
	}

	@Override
	public String getContentDetached() {
		return null;
	}

	@Override
	public void setContentDetached(String content) {
	}

	@Override
	public boolean isDefaultContent() {
		return true;
	}

	@Override
	public void setDefaultContent(boolean isDefaultContent) {
	}
}