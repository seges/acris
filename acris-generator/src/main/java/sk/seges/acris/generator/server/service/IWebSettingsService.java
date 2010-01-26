package sk.seges.acris.generator.server.service;

import sk.seges.acris.generator.server.WebSettings;

public interface IWebSettingsService {
	WebSettings getWebSettings(String webId, String lang);
}
