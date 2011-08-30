package sk.seges.acris.site.server.service.builder;

import sk.seges.acris.site.shared.domain.api.WebSettingsData;
import sk.seges.acris.site.shared.domain.dto.WebSettingsDTO;

public class NoTopLevelDomainWebSettingsBuilder implements IWebSettingsBuilder {

	@Override
	public WebSettingsData getWebSettings(String webId, Boolean localeSensitiveServer, String googleAnalyticsScript) {
		WebSettingsData webSettings = new WebSettingsDTO();
		webSettings.setWebId(webId);
		webSettings.setLanguage("en");

		webSettings.setAnalyticsScriptData(googleAnalyticsScript);

		return webSettings;
	}

}