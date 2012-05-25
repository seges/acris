package sk.seges.acris.site.server.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import sk.seges.acris.domain.shared.domain.ftp.server.model.data.FTPWebSettingsData;
import sk.seges.acris.site.server.dao.IWebSettingsDao;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;
import sk.seges.acris.site.shared.domain.api.WebSettingsData.MetaData;
import sk.seges.acris.site.shared.service.IWebSettingsService;
import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.corpis.server.domain.server.model.data.CountryData;
import sk.seges.corpis.server.service.ICountryService;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;


public class WebSettingsService implements IWebSettingsService {

	private IWebSettingsDao<? extends WebSettingsData> webSettingsDao;

	private ICountryService countryService;
	
	public WebSettingsService(IWebSettingsDao<? extends WebSettingsData> webSettingsDao, ICountryService countryService) {
		this.webSettingsDao = webSettingsDao;
		this.countryService = countryService;
	}

	protected WebSettingsData createDefaultSettings(String webId) {
		WebSettingsData webSettingsData = webSettingsDao.createDefaultEntity();
		webSettingsData.setWebId(webId);
		
		CountryData country = countryService.findDefaultCountry();

		if (country != null) {
			webSettingsData.setLanguage(country.getLanguage());
			Set<CountryData> translations = new HashSet<CountryData>();
			translations.add(country);
			webSettingsData.setTranslations(translations);
		}
		
		webSettingsData.setMetaData(new HashSet<MetaData>());

		return webSettingsData;
	}
	
	@Override
	public WebSettingsData getWebSettings(String webId) {
		Page page = new Page(0, 1);
		// TODO: switch to @BeanWrapper
		page.setFilterable(Filter.eq(HasWebId.WEB_ID).setValue(webId));
		List<WebSettingsData> result = webSettingsDao.findAll(page).getResult();
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
		webSettings.setFTPWebSettingsData(ftpWebSettings);
		saveWebSettings(webSettings);
	}

	@Override
	public FTPWebSettingsData getFTPWebSettings(String webId) {
		WebSettingsData webSettings = getWebSettings(webId);
		if (webSettings != null) {
			return webSettings.getFTPWebSettingsData();
		}
		return null;
	}	
}