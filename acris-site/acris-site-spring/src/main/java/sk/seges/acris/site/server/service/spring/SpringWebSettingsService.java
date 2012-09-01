package sk.seges.acris.site.server.service.spring;

import javax.validation.Valid;

import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.site.server.dao.IWebSettingsDao;
import sk.seges.acris.site.server.domain.api.server.model.data.WebSettingsData;
import sk.seges.acris.site.server.domain.ftp.server.model.data.FTPWebSettingsData;
import sk.seges.acris.site.server.service.WebSettingsService;
import sk.seges.corpis.server.service.ICountryService;
import sk.seges.corpis.service.annotation.TransactionPropagation;
import sk.seges.corpis.service.annotation.TransactionPropagation.PropagationType;
import sk.seges.sesam.pap.service.annotation.LocalService;


@LocalService
public class SpringWebSettingsService extends WebSettingsService {

	public SpringWebSettingsService(IWebSettingsDao<WebSettingsData> webSettings, ICountryService countryService) {
		super(webSettings, countryService);
	}

	@Transactional
	@TransactionPropagation(value = PropagationType.PROPAGATE)
	public void saveWebSettings(@Valid WebSettingsData webSettingsData) {
		super.saveWebSettings(webSettingsData);
	}	

	@Transactional
	@TransactionPropagation(value = PropagationType.PROPAGATE)
	public WebSettingsData getWebSettings(String webId) {
		return super.getWebSettings(webId);
	}

	@Transactional
	@TransactionPropagation(value = PropagationType.PROPAGATE)
	public void saveFTPWebSettings(String webId, FTPWebSettingsData ftpWebSettings) {
		super.saveFTPWebSettings(webId, ftpWebSettings);
	}
}