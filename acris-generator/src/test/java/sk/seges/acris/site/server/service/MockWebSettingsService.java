package sk.seges.acris.site.server.service;

import sk.seges.acris.site.ftp.server.model.data.FTPWebSettingsData;
import sk.seges.acris.site.server.model.data.WebSettingsData;
import sk.seges.acris.site.shared.service.IWebSettingsLocalService;

import java.util.List;

public class MockWebSettingsService implements IWebSettingsLocalService {

    private final WebSettingsData webSettings;

	public MockWebSettingsService(WebSettingsData webSettingsData) {
        this.webSettings = webSettingsData;
	}

	@Override
	public WebSettingsData getWebSettings(String webId) {
        if (webSettings.getWebId() != null && webSettings.getId().equals(webId)) {
            return webSettings;
        }

        webSettings.setWebId(webId);
        if (webId != null) {
            webSettings.setTopLevelDomain("http://" + webId + "/");
        }
        return webSettings;
	}

	@Override
	public void saveWebSettings(WebSettingsData webSettingsData) {
	}

	@Override
	public void saveFTPWebSettings(String webId, FTPWebSettingsData ftpWebSettings) {
	}

	@Override
	public FTPWebSettingsData getFTPWebSettings(String webId) {
		return null;
	}

	@Override
	public void deleteWebSettings(String webId) {
	}

	@Override
	public List<WebSettingsData> loadWebSettingsContainsParams(List<String> params) {
		return null;
	}

	@Override
	public WebSettingsData findWebSettings(String webId) {
		return null;
	}
}