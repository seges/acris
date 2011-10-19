package sk.seges.acris.generator.server.processor.mock;

import sk.seges.acris.site.shared.domain.api.WebSettingsData;
import sk.seges.acris.site.shared.domain.dto.WebSettingsDTO;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public class MockWebSettingsService implements IWebSettingsService {

	@Override
	public WebSettingsData getWebSettings(String webId) {
		WebSettingsDTO webSettings = new WebSettingsDTO();
		webSettings.setId("1");
		webSettings.setLanguage("en");
		webSettings.setWebId("mock");
		return webSettings;
	}

	@Override
	public void saveWebSettings(WebSettingsData webSettingsData) {
	}
}