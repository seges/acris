package sk.seges.acris.generator.server.processor;

import java.util.List;

import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.sesam.dao.Page;

public interface ContentDataProvider {

	List<String> getAvailableNiceurls(Page page);

	ContentData getContentForLanguage(ContentData contentData, String targetLanguage);
	ContentData getContent(GeneratorToken token);

	boolean exists(GeneratorToken token);
}