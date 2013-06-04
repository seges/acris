package sk.seges.acris.site.server.service.spring;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.site.server.dao.IWebSitesDao;
import sk.seges.acris.site.server.domain.api.server.model.data.WebSitesData;
import sk.seges.acris.site.server.service.WebSitesService;
import sk.seges.corpis.service.annotation.TransactionPropagation;
import sk.seges.corpis.service.annotation.TransactionPropagation.PropagationType;
import sk.seges.sesam.pap.service.annotation.LocalService;

/**
 * @author psloboda
 *
 */
@LocalService
public class SpringWebSitesService extends WebSitesService {

	public SpringWebSitesService(IWebSitesDao<WebSitesData> webSitesDao) {
		super(webSitesDao);
	}

	@Override
	@Transactional
	@TransactionPropagation(value = PropagationType.PROPAGATE)
	public List<WebSitesData> getWebSites(String webId) {
		return super.getWebSites(webId);
	}
	
	@Override
	@Transactional
	@TransactionPropagation(value = PropagationType.PROPAGATE)
	public void addWebSite(WebSitesData webSite) {
		super.addWebSite(webSite);
	}
	
	@Override
	@Transactional
	@TransactionPropagation(value = PropagationType.PROPAGATE)
	public WebSitesData getPrimaryWebSite(String webId, String locale) {
		return super.getPrimaryWebSite(webId, locale);
	}
}
