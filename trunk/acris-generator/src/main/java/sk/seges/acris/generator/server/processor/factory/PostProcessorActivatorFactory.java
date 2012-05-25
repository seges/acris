package sk.seges.acris.generator.server.processor.factory;

import sk.seges.acris.generator.server.processor.factory.api.OfflineWebSettingsFactory;
import sk.seges.acris.generator.server.processor.utils.PostProcessorActivator;
import sk.seges.acris.site.shared.domain.api.WebSettings;
import sk.seges.acris.site.shared.domain.api.server.model.data.WebSettingsData;

public class PostProcessorActivatorFactory {

	private OfflineWebSettingsFactory offlineWebSettingsFactory;

	public PostProcessorActivatorFactory(OfflineWebSettingsFactory offlineWebSettingsFactory) {
		this.offlineWebSettingsFactory = offlineWebSettingsFactory;
	}

	public PostProcessorActivator create(WebSettingsData webSettings) {
		return new PostProcessorActivator(offlineWebSettingsFactory.create(webSettings));
	}
}
