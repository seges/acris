package sk.seges.acris.site.server.service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import sk.seges.acris.site.ftp.server.model.data.FTPWebSettingsData;
import sk.seges.acris.site.server.dao.IWebSettingsDao;
import sk.seges.acris.site.server.model.data.MetaDataData;
import sk.seges.acris.site.server.model.data.WebSettingsData;
import sk.seges.corpis.server.domain.HasWebId;
import sk.seges.corpis.server.domain.server.model.data.CountryData;
import sk.seges.corpis.server.service.ICountryService;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;


public class WebSettingsService implements IWebSettingsServiceLocal {

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
			Set<CountryData> translations = new HashSet<CountryData>();
			translations.add(country);
			webSettingsData.setTranslations(translations);
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
}
