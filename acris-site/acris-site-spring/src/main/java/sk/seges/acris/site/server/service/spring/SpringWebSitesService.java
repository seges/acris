package sk.seges.acris.site.server.service.spring;

import org.springframework.transaction.annotation.Transactional;
import sk.seges.acris.site.server.dao.IWebSitesDao;
import sk.seges.acris.site.server.model.data.WebSitesData;
import sk.seges.acris.site.server.service.WebSitesService;
import sk.seges.corpis.service.annotation.PropagationType;
import sk.seges.corpis.service.annotation.TransactionPropagation;
import sk.seges.sesam.pap.service.annotation.LocalService;

import java.util.List;

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
	
	@Override
	@Transactional
	@TransactionPropagation(value = PropagationType.PROPAGATE)
	public List<WebSitesData> findWebSitesByDomain(String domain){
		return super.findWebSitesByDomain(domain);
	}
	
	@Override
	@Transactional
	@TransactionPropagation(value = PropagationType.PROPAGATE)
	public void deleteWebSites(String webId){
		super.deleteWebSites(webId);
	}
	
	@Override
	@Transactional
	@TransactionPropagation(value = PropagationType.PROPAGATE)
	public void deleteWebSites(WebSitesData webSite){
		super.deleteWebSites(webSite);
	}
	
	@Override
	@Transactional
	@TransactionPropagation(value = PropagationType.PROPAGATE)
	public void deleteWebSites(String webId, String domain) {
		super.deleteWebSites(webId, domain);
	}
}

