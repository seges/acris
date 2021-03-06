package sk.seges.acris.generator.server.processor.factory.api;

import sk.seges.acris.generator.server.manager.api.OfflineWebSettings;
import sk.seges.acris.site.server.model.data.WebSettingsData;

public interface OfflineWebSettingsFactory {

	OfflineWebSettings create(WebSettingsData webSettings);
}
