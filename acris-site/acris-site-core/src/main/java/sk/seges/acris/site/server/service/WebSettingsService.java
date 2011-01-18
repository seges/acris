package sk.seges.acris.site.server.service;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import sk.seges.acris.site.server.dao.IWebSettingsDao;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;
import sk.seges.acris.site.shared.domain.api.WebSettingsData.MetaData;
import sk.seges.acris.site.shared.service.IWebSettingsService;
import sk.seges.corpis.shared.domain.api.CountryData;
import sk.seges.corpis.shared.domain.api.HasWebId;
import sk.seges.corpis.shared.service.ICountryService;
import sk.seges.sesam.dao.Filter;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.dao.SimpleExpression;


public class WebSettingsService implements IWebSettingsService {

	private IWebSettingsDao<? extends WebSettingsData> webSettingsDao;

	private ICountryService countryService;
	
	public WebSettingsService(IWebSettingsDao<? extends WebSettingsData> webSettings, ICountryService countryService) {
		this.webSettingsDao = webSettings;
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
		SimpleExpression<Comparable<? extends Serializable>> eq = Filter.eq(HasWebId.WEB_ID);
		eq.setValue(webId);
		page.setFilterable(eq);
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
}