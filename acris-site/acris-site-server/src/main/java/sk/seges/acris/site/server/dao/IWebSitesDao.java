package sk.seges.acris.site.server.dao;

import java.util.List;

import org.hibernate.criterion.DetachedCriteria;

import sk.seges.acris.site.server.model.data.WebSitesData;
import sk.seges.sesam.dao.ICrudDAO;
import sk.seges.sesam.dao.Page;

public interface IWebSitesDao <T extends WebSitesData> extends ICrudDAO<T> {

	WebSitesData createDefaultEntity();
	
	WebSitesData loadWebSitesByDomain(String domain);
	
	List<WebSitesData> findByCriteria(DetachedCriteria criteria, Page allResultsPage);
	
	List<String> findWebSiteWebIds(String domain);
}
