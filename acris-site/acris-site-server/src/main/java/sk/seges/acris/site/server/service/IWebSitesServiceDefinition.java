package sk.seges.acris.site.server.service;


public interface IWebSitesServiceDefinition extends IWebSitesServiceLocal {

	void deleteWebSites(String webId, String domain);	
}
