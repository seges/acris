package sk.seges.acris.site.server.service;

import sk.seges.acris.domain.shared.domain.ftp.server.model.data.FTPWebSettingsData;
import sk.seges.acris.site.server.service.builder.IWebSettingsBuilder;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public class MockWebSettingsService implements IWebSettingsService {

	protected Boolean localeSensitiveServer;

	protected String googleAnalyticsScript;

	protected IWebSettingsBuilder webSettingsBuilder;
	
	public MockWebSettingsService(IWebSettingsBuilder webSettingsBuilder, String googleAnalyticsScript, Boolean localeSensitiveServer) {
		this.webSettingsBuilder = webSettingsBuilder;
		this.googleAnalyticsScript = googleAnalyticsScript;
		this.localeSensitiveServer = localeSensitiveServer;
	}

	public void setWebSettingsBuilder(IWebSettingsBuilder webSettingsBuilder) {
		this.webSettingsBuilder = webSettingsBuilder;
	}

	public IWebSettingsBuilder getWebSettingsBuilder() {
		return webSettingsBuilder;
	}
	
	public Boolean getLocaleSensitiveServer() {
		return localeSensitiveServer;
	}

	public void setLocaleSensitiveServer(Boolean localeSensitiveServer) {
		this.localeSensitiveServer = localeSensitiveServer;
	}

	public String getGoogleAnalyticsScript() {
		return googleAnalyticsScript;
	}

	public void setGoogleAnalyticsScript(String googleAnalyticsScript) {
		this.googleAnalyticsScript = googleAnalyticsScript;
	}

	@Override
	public WebSettingsData getWebSettings(String webId) {
		return webSettingsBuilder.getWebSettings(webId, localeSensitiveServer, googleAnalyticsScript);
	}

	@Override
	public void saveWebSettings(WebSettingsData webSettingsData) {
	}

	@Override
	public void saveFTPWebSettings(String webId, FTPWebSettingsData ftpWebSettings) {
		// TODO Auto-generated method stub
	}

	@Override
	public FTPWebSettingsData getFTPWebSettings(String webId) {
		// TODO Auto-generated method stub
		return null;
	}
}