package sk.seges.acris.site.shared.service;

import java.util.List;

import sk.seges.acris.shared.model.dto.WebSitesDTO;
import sk.seges.acris.site.shared.domain.api.SiteType;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;

import com.google.gwt.user.client.rpc.RemoteService;

@RemoteServiceDefinition
public interface IWebSitesService extends RemoteService {

	List<WebSitesDTO> getWebSites(String webId);
	
	void addWebSite(WebSitesDTO webSite);
	
	WebSitesDTO getPrimaryWebSite(String webId, String locale);
	
	WebSitesDTO getWebSite(String webId, String locale, SiteType siteType);
	
	void deleteWebSites(String webId);
	
	void deleteWebSites(WebSitesDTO webSite);
	
	List<String> getCurrentDomainNames(String webId, String locale);
	
	List<WebSitesDTO> findWebSitesByDomain(String domain);
}
