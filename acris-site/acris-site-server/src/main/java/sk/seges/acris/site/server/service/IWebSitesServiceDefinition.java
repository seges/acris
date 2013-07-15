package sk.seges.acris.site.server.service;

import java.util.List;

import sk.seges.acris.site.server.model.data.WebSitesData;

public interface IWebSitesServiceDefinition extends IWebSitesServiceLocal {

	List<WebSitesData> findWebSitesByDomain(String domain);

	void deleteWebSites(WebSitesData webSite);
	
	void deleteWebSites(String webId, String domain);	
}
