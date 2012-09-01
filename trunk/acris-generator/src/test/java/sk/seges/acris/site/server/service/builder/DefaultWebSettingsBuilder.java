package sk.seges.acris.site.server.service.builder;

import sk.seges.acris.site.server.domain.api.server.model.data.WebSettingsData;
import sk.seges.acris.site.shared.domain.jpa.JpaWebSettings;

public class DefaultWebSettingsBuilder implements IWebSettingsBuilder {

	@Override
	public WebSettingsData getWebSettings(String webId, Boolean localeSensitiveServer, String googleAnalyticsScript) {
		WebSettingsData webSettings = new JpaWebSettings();
		webSettings.setWebId(webId);
		webSettings.setLanguage("en");

		webSettings.setTopLevelDomain("http://" + webId + "/");

		webSettings.setAnalyticsScriptData(googleAnalyticsScript);

		return webSettings;
	}

}