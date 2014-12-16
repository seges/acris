package sk.seges.acris.site.server.service;

import java.util.ArrayList;
import java.util.List;

import sk.seges.acris.site.server.dao.IWebSitesDao;
import sk.seges.acris.site.server.model.data.WebSitesData;
import sk.seges.acris.site.shared.domain.api.SiteType;
import sk.seges.corpis.shared.domain.HasLanguage;
import sk.seges.corpis.shared.domain.HasWebId;
import sk.seges.sesam.dao.Conjunction;
import sk.seges.sesam.dao.Disjunction;
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
		Conjunction c = Filter.conjunction();
		c.add(Filter.eq(HasWebId.WEB_ID, webId));
		c.add(Filter.eq(WebSitesData.TYPE, SiteType.PRIMARY_PENDING));
		c.add(Filter.eq(WebSitesData.TYPE, SiteType.PENDING));
		page.setFilterable(c);
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
	public WebSitesData  getWebSite(String webId, String locale, SiteType siteType) {
		Page page = new Page(0,1);
		Conjunction c = Filter.conjunction();
		c.add(Filter.eq(HasWebId.WEB_ID, webId));
		c.add(Filter.eq(HasLanguage.LANGUAGE, locale));
		c.add(Filter.eq(WebSitesData.TYPE, siteType));
		page.setFilterable(c);
		return webSitesDao.findUnique(page);
	}
	
	@Override
	public List<WebSitesData> loadWebSites(SiteType... siteType) {
		Page page = new Page(0, Page.ALL_RESULTS);
		Disjunction dis = Filter.disjunction();
		for (SiteType type : siteType) {
			dis.add(Filter.eq(WebSitesData.TYPE, type));
		}
		page.setFilterable(dis);
		return webSitesDao.findAll(page).getResult();
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
	public void deleteWebSites(WebSitesData webSite) {
		if (webSite == null) return;
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
	
	@Override
	public List<String> getCurrentDomainNames(String webId, String locale){
		Page page = Page.ALL_RESULTS_PAGE;
		Conjunction conjunction = Filter.conjunction();
		conjunction.add(Filter.eq(HasWebId.WEB_ID).setValue(webId));
		if (locale != null) {
			conjunction.add(Filter.eq(WebSitesData.LANGUAGE).setValue(locale));
		}
		conjunction.add(Filter.eq(WebSitesData.TYPE, SiteType.PRIMARY_PENDING));
		conjunction.add(Filter.eq(WebSitesData.TYPE, SiteType.PENDING));
		page.setFilterable(conjunction);
		 
		List<WebSitesData> webSitesList = webSitesDao.findAll(page).getResult();;
		List<String> domainNames = new ArrayList<String>();
		if(webSitesList != null && !webSitesList.isEmpty()){
			for(WebSitesData webSite : webSitesList){
				domainNames.add(webSite.getDomain());
			}
		}
		return domainNames;
	}
}