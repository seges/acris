package sk.seges.acris.generator.server.processor.post.alters;

import sk.seges.acris.site.shared.service.IWebSettingsService;


public class DescriptionMetaTagPostProcessor extends AbstractMetaTagPostProcessor {

	public DescriptionMetaTagPostProcessor(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

	private static final String DESCRIPTION_TAG_NAME = "description";

	@Override
	protected String getMetaTagName() {
		return DESCRIPTION_TAG_NAME;
	}

	@Override
	protected String getMetaTagContent() {
		return contentInfoProvider.getContentDescription(generatorToken);
	}
}