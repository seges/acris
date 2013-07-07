package sk.seges.acris.site.server.service;

import sk.seges.acris.site.server.model.data.WebSitesData;

import java.util.List;

public interface IWebSitesServiceDefinition extends IWebSitesServiceLocal {

	List<WebSitesData> findWebSitesByDomain(String domain);

	void deleteWebSites(WebSitesData webSite);
}
