package sk.seges.acris.generator.server.processor.post.alters;

import sk.seges.acris.generator.server.processor.IContentInfoProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public abstract class AbstractContentInfoPostProcessor extends AbstractElementPostProcessor {

	protected IContentInfoProvider contentInfoProvider;

	protected AbstractContentInfoPostProcessor(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

	public void setContentInfoProvider(IContentInfoProvider contentInfoProvider) {
		this.contentInfoProvider = contentInfoProvider;
	}
}
