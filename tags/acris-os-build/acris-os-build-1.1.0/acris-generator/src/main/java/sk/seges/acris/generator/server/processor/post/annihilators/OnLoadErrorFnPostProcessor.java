package sk.seges.acris.generator.server.processor.post.annihilators;

import sk.seges.acris.site.shared.service.IWebSettingsService;

public class OnLoadErrorFnPostProcessor extends AbstractMetaTagPostProcessor {

	public OnLoadErrorFnPostProcessor(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

	private static final String ON_LOAD_ERROR_META_TAG_NAME = "gwt:onLoadErrorFn";
	
	@Override
	protected String getMetaTagName() {
		return ON_LOAD_ERROR_META_TAG_NAME;
	}
}