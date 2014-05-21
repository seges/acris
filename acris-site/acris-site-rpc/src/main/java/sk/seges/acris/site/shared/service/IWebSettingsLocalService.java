package sk.seges.acris.site.shared.service;

import java.util.List;

import sk.seges.acris.site.ftp.server.model.data.FTPWebSettingsData;
import sk.seges.acris.site.server.model.data.WebSettingsData;
import sk.seges.acris.site.server.service.IWebSettingsRemoteServiceLocal;

public interface IWebSettingsLocalService extends IWebSettingsRemoteServiceLocal {
	
	WebSettingsData findWebSettings(String webId);
	
	void deleteWebSettings(String webId);
	
	FTPWebSettingsData getFTPWebSettings(String webId);
	
	List<WebSettingsData> loadWebSettingsContainsParams(List<String> params);
	
	void saveFTPWebSettings(String webId, FTPWebSettingsData ftpWebSettings);

}
