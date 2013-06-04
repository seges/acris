package sk.seges.corpis.server.dao.hibernate.spring;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.site.server.dao.hibernate.HibernateWebSitesDao;
import sk.seges.acris.site.server.domain.api.server.model.data.WebSitesData;

@Transactional
public class SpringHibernateWebSitesDao extends HibernateWebSitesDao {

	@Override
	@Transactional
	public WebSitesData loadWebSitesByDomain(String domain) {
		return super.loadWebSitesByDomain(domain);
	}
}
