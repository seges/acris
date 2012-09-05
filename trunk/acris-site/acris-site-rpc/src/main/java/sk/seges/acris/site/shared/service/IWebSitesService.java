package sk.seges.acris.site.shared.service;

import java.util.List;

import sk.seges.acris.shared.model.dto.WebSitesDTO;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

import com.google.gwt.user.client.rpc.RemoteService;

@RemoteServiceDefinition
public interface IWebSitesService extends RemoteService {

	List<WebSitesDTO> getWebSites(String webId);
	
	void addWebSite(WebSitesDTO webSite);
	
	WebSitesDTO getPrimaryWebSite(String webId, String locale);
}
