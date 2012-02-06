package sk.seges.acris.site.shared.service;

import sk.seges.acris.domain.shared.domain.ftp.server.model.data.FTPWebSettingsData;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IWebSettingsService extends RemoteService {

	WebSettingsData getWebSettings(String webId);
	
	void saveWebSettings(WebSettingsData webSettingsData);
	
	void saveFTPWebSettings(String webId, FTPWebSettingsData ftpWebSettings);
	
	FTPWebSettingsData getFTPWebSettings(String webId);
}