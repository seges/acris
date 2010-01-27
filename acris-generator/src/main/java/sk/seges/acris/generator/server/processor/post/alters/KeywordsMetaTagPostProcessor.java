package sk.seges.acris.generator.server.processor.post.alters;

import org.springframework.stereotype.Component;

@Component
public class KeywordsMetaTagPostProcessor extends AbstractMetaTagPostProcessor {

	private static final String KEYWORDS_TAG_NAME = "keywords";

	@Override
	protected String getMetaTagName() {
		return KEYWORDS_TAG_NAME;
	}

	@Override
	protected String getMetaTagContent() {
		return contentInfoProvider.getContentKeywords(generatorToken);
	}
}