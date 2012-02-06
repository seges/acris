package sk.seges.acris.site.server.service.spring;

import javax.validation.Valid;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.domain.shared.domain.ftp.server.model.data.FTPWebSettingsData;
import sk.seges.acris.site.server.dao.IWebSettingsDao;
import sk.seges.acris.site.server.service.WebSettingsService;
import sk.seges.acris.site.shared.domain.api.WebSettingsData;
import sk.seges.corpis.shared.service.ICountryService;


public class SpringWebSettingsService extends WebSettingsService {

	public SpringWebSettingsService(IWebSettingsDao<? extends WebSettingsData> webSettings, ICountryService countryService) {
		super(webSettings, countryService);
	}

	@Transactional
	public void saveWebSettings(@Valid WebSettingsData webSettingsData) {
		super.saveWebSettings(webSettingsData);
	}	

	@Transactional
	public WebSettingsData getWebSettings(String webId) {
		return super.getWebSettings(webId);
	}

	@Transactional
	public void saveFTPWebSettings(String webId, FTPWebSettingsData ftpWebSettings) {
		super.saveFTPWebSettings(webId, ftpWebSettings);
	}
}