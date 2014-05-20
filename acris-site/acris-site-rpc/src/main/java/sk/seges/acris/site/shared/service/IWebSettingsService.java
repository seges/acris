package sk.seges.acris.site.shared.service;

import java.util.List;

import sk.seges.acris.shared.model.dto.FTPWebSettingsDTO;
import sk.seges.acris.shared.model.dto.WebSettingsDTO;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

import com.google.gwt.user.client.rpc.RemoteService;

@RemoteServiceDefinition
public interface IWebSettingsService extends RemoteService {

	WebSettingsDTO getWebSettings(String webId);
	
	WebSettingsDTO findWebSettings(String webId);
	
	void saveWebSettings(WebSettingsDTO webSettingsData);
	
	void saveFTPWebSettings(String webId, FTPWebSettingsDTO ftpWebSettings);
	
	FTPWebSettingsDTO getFTPWebSettings(String webId);
	
	void deleteWebSettings(String webId);
	
	List<WebSettingsDTO> loadWebSettingsContainsParams(List<String> params);
}