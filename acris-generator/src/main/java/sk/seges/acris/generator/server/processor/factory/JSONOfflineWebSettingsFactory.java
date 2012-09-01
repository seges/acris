package sk.seges.acris.generator.server.processor.factory;

import sk.seges.acris.generator.server.manager.JSONOfflineWebSettings;
import sk.seges.acris.generator.server.manager.api.OfflineWebSettings;
import sk.seges.acris.generator.server.processor.factory.api.OfflineWebSettingsFactory;
import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;
import sk.seges.acris.site.server.domain.api.server.model.data.WebSettingsData;

public class JSONOfflineWebSettingsFactory implements OfflineWebSettingsFactory {

	private ParametersManagerFactory parameterManagerFactory;

	public JSONOfflineWebSettingsFactory(ParametersManagerFactory parameterManagerFactory) {
		this.parameterManagerFactory = parameterManagerFactory;
	}

	@Override
	public OfflineWebSettings create(WebSettingsData webSettings) {
		return new JSONOfflineWebSettings(webSettings, parameterManagerFactory);
	}
}