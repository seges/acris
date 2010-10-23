package sk.seges.acris.generator.server.processor.post.alters;

import sk.seges.acris.site.shared.service.IWebSettingsService;

public class KeywordsMetaTagPostProcessor extends AbstractMetaTagPostProcessor {

	public KeywordsMetaTagPostProcessor(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

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