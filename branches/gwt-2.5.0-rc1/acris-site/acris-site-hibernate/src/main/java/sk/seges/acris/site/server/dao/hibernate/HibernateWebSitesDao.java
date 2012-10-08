package sk.seges.acris.site.server.dao.hibernate;

import sk.seges.acris.site.server.dao.IWebSitesDao;
import sk.seges.acris.site.server.domain.api.server.model.data.WebSitesData;
import sk.seges.acris.site.server.domain.jpa.JpaWebSites;
import sk.seges.corpis.dao.hibernate.AbstractHibernateCRUD;

public class HibernateWebSitesDao extends AbstractHibernateCRUD<WebSitesData> implements IWebSitesDao<WebSitesData> {

	public HibernateWebSitesDao() {
		super(JpaWebSites.class);
	}

	@Override
	public WebSitesData createDefaultEntity() {
		return new JpaWebSites();
	}
}
