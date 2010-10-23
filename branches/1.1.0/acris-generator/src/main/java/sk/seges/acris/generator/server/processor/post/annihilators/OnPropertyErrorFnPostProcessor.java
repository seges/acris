package sk.seges.acris.generator.server.processor.post.annihilators;

import sk.seges.acris.site.shared.service.IWebSettingsService;

public class OnPropertyErrorFnPostProcessor extends AbstractMetaTagPostProcessor {

	public OnPropertyErrorFnPostProcessor(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

	private static final String ON_PROPERTY_ERROR_META_TAG_NAME = "gwt:onPropertyErrorFn";

	@Override
	protected String getMetaTagName() {
		return ON_PROPERTY_ERROR_META_TAG_NAME;
	}
}