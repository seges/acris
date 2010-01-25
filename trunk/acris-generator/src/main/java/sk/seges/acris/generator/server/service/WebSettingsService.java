package sk.seges.acris.generator.server.service;

import net.sf.gilead.gwt.PersistentRemoteService;

import org.springframework.stereotype.Service;

import sk.seges.acris.generator.server.WebSettings;

@Service
public class WebSettingsService extends PersistentRemoteService implements IWebSettingsService {

	private static final long serialVersionUID = 1337432311249783469L;

	@Override
	public WebSettings getWebSettings(String webId, String language) {
		WebSettings webSettings = new WebSettings();
		webSettings.setLanguage(language);
		webSettings.setWebId(webId);
		
		webSettings.setTopLevelDomain("http://" + webId + "/");
		
		return webSettings;
	}
}