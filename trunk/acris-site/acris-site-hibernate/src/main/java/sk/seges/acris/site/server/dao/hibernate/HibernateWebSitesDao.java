package sk.seges.acris.site.server.dao.hibernate;

import sk.seges.acris.site.server.dao.IWebSitesDao;
import sk.seges.acris.site.server.domain.api.server.model.data.WebSitesData;
import sk.seges.acris.site.server.domain.jpa.JpaWebSites;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;
import sk.seges.sesam.dao.Conjunction;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;

public class HibernateWebSitesDao extends AbstractHibernateCRUD<WebSitesData> implements IWebSitesDao<WebSitesData> {

	public HibernateWebSitesDao() {
		super(JpaWebSites.class);
	}

	@Override
	public WebSitesData createDefaultEntity() {
		return new JpaWebSites();
	}

	@Override
	public WebSitesData loadWebSitesByDomain(String domain) {
		Page page = new Page(0,1);
		Conjunction c = Filter.conjunction();
		c.add(Filter.eq(WebSitesData.DOMAIN, domain));
		page.setFilterable(c);
		
		return findUnique(page);
	}
}
