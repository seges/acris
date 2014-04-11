package sk.seges.acris.site.server.service;

import java.util.HashSet;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.seges.acris.site.ftp.server.model.data.FTPWebSettingsData;
import sk.seges.acris.site.server.dao.IWebSettingsDao;
import sk.seges.acris.site.server.model.data.MetaDataData;
import sk.seges.acris.site.server.model.data.WebSettingsData;
import sk.seges.corpis.server.domain.server.model.data.CountryData;
import sk.seges.corpis.server.service.ICountryService;
import sk.seges.corpis.shared.domain.HasWebId;
import sk.seges.sesam.dao.Disjunction;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;


public class WebSettingsService implements IWebSettingsServiceLocal {
	private static final Logger log = LoggerFactory.getLogger(WebSettingsService.class);
	
	private IWebSettingsDao<WebSettingsData> webSettingsDao;

	private ICountryService countryService;
	
	public WebSettingsService(IWebSettingsDao<WebSettingsData> webSettingsDao, ICountryService countryService) {
		this.webSettingsDao = webSettingsDao;
		this.countryService = countryService;
	}

	protected WebSettingsData createDefaultSettings(String webId) {
		WebSettingsData webSettingsData = webSettingsDao.createDefaultEntity();
		webSettingsData.setWebId(webId);
		
		CountryData country = countryService.findDefaultCountry();

		if (country != null) {
			webSettingsData.setLanguage(country.getLanguage());
		}
		
		webSettingsData.setMetaData(new HashSet<MetaDataData>());

		return webSettingsData;
	}
	
	@Override
	public WebSettingsData getWebSettings(String webId) {
		Page page = new Page(0, 1);
		// TODO: switch to @BeanWrapper
		page.setFilterable(Filter.eq(HasWebId.WEB_ID).setValue(webId));
		List<? extends WebSettingsData> result = webSettingsDao.findAll(page).getResult();
		if (result.size() == 0) {
			return createDefaultSettings(webId);
		}
		
		WebSettingsData webSettings = result.get(0);
		webSettings.getMetaData().size();
		return webSettings;
	}

	@Override
	public void saveWebSettings(@Valid WebSettingsData webSettingsData) {
		webSettingsData = webSettingsDao.merge(webSettingsData);
	}

	@Override
	public void saveFTPWebSettings(String webId, FTPWebSettingsData ftpWebSettings) {
		WebSettingsData webSettings = getWebSettings(webId);
		webSettings.setFtpWebSettings(ftpWebSettings);
		saveWebSettings(webSettings);
	}

	@Override
	public FTPWebSettingsData getFTPWebSettings(String webId) {
		WebSettingsData webSettings = getWebSettings(webId);
		if (webSettings != null) {
			return webSettings.getFtpWebSettings();
		}
		return null;
	}

	@Override
	public void deleteWebSettings(String webId) {
		WebSettingsData entity = webSettingsDao.createDefaultEntity();
		entity.setWebId(webId);
		entity = webSettingsDao.findEntity(entity);
		if(entity == null) {
			log.debug("No such web settings exist for webId = " + webId);
			return;
		}
		webSettingsDao.remove(entity);
	}

	@Override
	public List<WebSettingsData> loadWebSettingsContainsParams(List<String> params) {
		Page page = new Page(0, Page.ALL_RESULTS);
		Disjunction disjunction = Filter.disjunction();
		for (String param : params) {
			disjunction.add(Filter.like(WebSettingsData.PARAMETERS).setValue(param));
		}
		page.setFilterable(disjunction);
		return webSettingsDao.findAll(page).getResult();
	}	
}
