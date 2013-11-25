package sk.seges.acris.site.shared.service;

import java.util.List;

import sk.seges.acris.shared.model.dto.FTPWebSettingsDTO;
import sk.seges.acris.shared.model.dto.WebSettingsDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface IWebSettingsServiceAsync {

	void getWebSettings(String webId, AsyncCallback<WebSettingsDTO> callback);
	
	void findWebSettings(String webId, AsyncCallback<WebSettingsDTO> callback);

	void saveWebSettings(WebSettingsDTO webSettingsData, AsyncCallback<Void> callback);
	
	void saveFTPWebSettings(String webId, FTPWebSettingsDTO ftpWebSettings, AsyncCallback<Void> callback);
	
	void getFTPWebSettings(String webId, AsyncCallback<FTPWebSettingsDTO> callback);
	
	void deleteWebSettings(String webId, AsyncCallback<Void> callback);
	
	void loadWebSettingsContainsParams(List<String> params, AsyncCallback<List<WebSettingsDTO>> callback);
}