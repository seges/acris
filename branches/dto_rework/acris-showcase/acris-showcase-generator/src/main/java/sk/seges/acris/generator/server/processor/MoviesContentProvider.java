package sk.seges.acris.generator.server.processor;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.showcase.mora.client.configuration.NameTokens;


public class MoviesContentProvider implements ContentDataProvider {

	@Override
	public List<String> getAvailableNiceurls(String lang, String webId) {
		List<String> result = new ArrayList<String>();
		result.add(NameTokens.HOME_PAGE);
		return result;
	}

	@Override
	public ContentData<?> getContentForLanguage(ContentData<?> contentData, String targetLanguage) {
		return new MoviesContent();
	}

	@Override
	public ContentData<?> getContent(GeneratorToken token) {
		return new MoviesContent();
	}

	@Override
	public boolean exists(GeneratorToken token) {
		return true;
	}
}