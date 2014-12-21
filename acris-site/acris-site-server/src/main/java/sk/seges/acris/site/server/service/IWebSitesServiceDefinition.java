package sk.seges.acris.site.server.service;

import java.util.List;

import sk.seges.acris.site.server.model.data.WebSitesData;
import sk.seges.acris.site.shared.domain.api.SiteType;


public interface IWebSitesServiceDefinition extends IWebSitesServiceLocal {

	void deleteWebSites(String webId, String domain);	
	
	List<WebSitesData> loadWebSites(SiteType... siteType);
}
