package sk.seges.acris.site.server.dao;

import sk.seges.acris.site.server.model.data.WebSitesData;
import sk.seges.sesam.dao.ICrudDAO;

public interface IWebSitesDao <T extends WebSitesData> extends ICrudDAO<T> {

	WebSitesData createDefaultEntity();
	
	WebSitesData loadWebSitesByDomain(String domain);
}
