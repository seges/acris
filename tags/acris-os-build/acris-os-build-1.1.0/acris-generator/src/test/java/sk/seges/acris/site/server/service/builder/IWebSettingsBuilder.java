package sk.seges.acris.site.server.service.builder;

import sk.seges.acris.site.shared.domain.api.WebSettingsData;


public interface IWebSettingsBuilder {

	WebSettingsData getWebSettings(String webId, Boolean localeSensitiveServer, String googleAnalyticsScript);

}
