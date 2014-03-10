package sk.seges.acris.site.server.service;

import java.util.List;

import sk.seges.acris.site.server.dao.IWebSitesDao;
import sk.seges.acris.site.server.model.data.WebSitesData;
import sk.seges.acris.site.shared.domain.api.SiteType;
import sk.seges.corpis.shared.domain.HasLanguage;
import sk.seges.corpis.shared.domain.HasWebId;
import sk.seges.sesam.dao.Conjunction;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.PagedResult;

/**
 * @author psloboda
 *
 */
public class WebSitesService implements IWebSitesServiceDefinition {

	private IWebSitesDao<WebSitesData> webSitesDao;
	
	public WebSitesService(IWebSitesDao<WebSitesData> webSitesDao) {
		this.webSitesDao = webSitesDao;
	}

	@Override
	public List<WebSitesData> getWebSites(String webId) {
		Page page = Page.ALL_RESULTS_PAGE;
		page.setFilterable(Filter.eq(HasWebId.WEB_ID).setValue(webId));
		return webSitesDao.findAll(page).getResult();
	}

	@Override
	public void addWebSite(WebSitesData webSite) {
		if (webSite.getType().equals(SiteType.PRIMARY))	{
			WebSitesData oldPrimary = getPrimaryWebSite(webSite.getWebId(), webSite.getLanguage());
			if (oldPrimary != null) {
				oldPrimary.setType(SiteType.ALIAS);
				webSitesDao.merge(oldPrimary);
			}
		} 
		webSitesDao.persist(webSite);
	}

	@Override
	public WebSitesData getPrimaryWebSite(String webId, String locale) {
		Page page = new Page(0,1);
		Conjunction c = Filter.conjunction();
		c.add(Filter.eq(HasWebId.WEB_ID, webId));
		c.add(Filter.eq(HasLanguage.LANGUAGE, locale));
		c.add(Filter.eq(WebSitesData.TYPE, SiteType.PRIMARY));
		page.setFilterable(c);
		
		return webSitesDao.findUnique(page);
	}

	@Override
	public void deleteWebSites(String webId) {
		Page page = new Page();
		page.setFilterable(Filter.eq(HasWebId.WEB_ID, webId));
		
		PagedResult<List<WebSitesData>> all = webSitesDao.findAll(page);
		for(WebSitesData webSite : all.getResult()) {
			webSitesDao.remove(webSite);
		}
	}

	@Override
	public List<WebSitesData> findWebSitesByDomain(String domain) {
		Page page = new Page(0, Page.ALL_RESULTS);
		page.setFilterable(Filter.eq(WebSitesData.DOMAIN, domain));
		return webSitesDao.findAll(page).getResult();		
	}

	@Override
	public void deleteWebSites(WebSitesData webSite){
		WebSitesData webS = webSitesDao.findEntity(webSite);
		webSitesDao.remove(webS);
	}

	@Override
	public void deleteWebSites(String webId, String domain) {
		Page page = new Page();
		Conjunction c = Filter.conjunction();
		c.add(Filter.eq(WebSitesData.DOMAIN, domain));
		c.add(Filter.eq(WebSitesData.WEB_ID, webId));
		page.setFilterable(c);
		PagedResult<List<WebSitesData>> all = webSitesDao.findAll(page);
		for(WebSitesData webSite : all.getResult()) {
			webSitesDao.remove(webSite);
		}
	}
}