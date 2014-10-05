package sk.seges.acris.generator.server.processor;

import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.site.server.domain.api.ContentData;
import sk.seges.sesam.dao.Page;

import java.util.List;

public interface ContentDataProvider {

	List<String> getAvailableNiceurls(Page page);

	ContentData getContentForLanguage(ContentData contentData, String targetLanguage);
	ContentData getContent(GeneratorToken token);
	ContentData getHomeContent(String targetLanguage, String webId);

	boolean exists(GeneratorToken token);
}