package sk.seges.acris.site.server.service.builder;

import sk.seges.acris.site.server.domain.jpa.JpaWebSettings;
import sk.seges.acris.site.server.model.data.WebSettingsData;

public class NoTopLevelDomainWebSettingsBuilder implements IWebSettingsBuilder {

	@Override
	public WebSettingsData getWebSettings(String webId, Boolean localeSensitiveServer, String googleAnalyticsScript) {
		WebSettingsData webSettings = new JpaWebSettings();
		webSettings.setWebId(webId);
		webSettings.setLanguage("en");

		webSettings.setAnalyticsScriptData(googleAnalyticsScript);

		return webSettings;
	}

}