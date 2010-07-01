package sk.seges.acris.generator.server.service;

import net.sf.gilead.gwt.PersistentRemoteService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import sk.seges.acris.generator.server.WebSettings;

@Service
public class WebSettingsService extends PersistentRemoteService implements IWebSettingsService {

	private static final long serialVersionUID = 1337432311249783469L;

	@Autowired
	@Qualifier("locale.sensitive.server")
	protected Boolean localeSensitiveServer;

	@Autowired
	@Qualifier("google.analytics.script")
	protected String googleAnalyticsScript;


	public Boolean getLocaleSensitiveServer() {
        return localeSensitiveServer;
    }


    public void setLocaleSensitiveServer(Boolean localeSensitiveServer) {
        this.localeSensitiveServer = localeSensitiveServer;
    }


    public String getGoogleAnalyticsScript() {
        return googleAnalyticsScript;
    }


    public void setGoogleAnalyticsScript(String googleAnalyticsScript) {
        this.googleAnalyticsScript = googleAnalyticsScript;
    }


    @Override
	public WebSettings getWebSettings(String webId, String lang) {
		WebSettings webSettings = new WebSettings();
		webSettings.setWebId(webId);
		webSettings.setLang(lang);

		//now we are not supporting localeSensisteServer
		webSettings.setTopLevelDomain("http://" + webId + "/");
		
		webSettings.setGoogleAnalyticsScript(googleAnalyticsScript);
		
		return webSettings;
	}
}