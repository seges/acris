package sk.seges.acris.site.server.service.builder;

import sk.seges.acris.generator.server.manager.data.OfflineGeneratorParameter;
import sk.seges.acris.generator.server.processor.post.annihilators.NochacheScriptAnnihilatorPostProcessor;
import sk.seges.acris.site.shared.domain.api.server.model.data.WebSettingsData;
import sk.seges.acris.site.shared.domain.jpa.JpaWebSettings;

public class NocacheScriptWebSettingsBuilder implements IWebSettingsBuilder {

	@Override
	public WebSettingsData getWebSettings(String webId, Boolean localeSensitiveServer, String googleAnalyticsScript) {
		WebSettingsData webSettings = new JpaWebSettings();
		webSettings.setWebId(webId);
		webSettings.setLanguage("en");

		webSettings.setParameters(OfflineGeneratorParameter.INACTIVE_PROCESSORS.getKey() + "=" + NochacheScriptAnnihilatorPostProcessor.class.getSimpleName() + ";" +
								  OfflineGeneratorParameter.INACTIVE_INDEX_PROCESSORS.getKey() + "=");
		
		webSettings.setTopLevelDomain("http://" + webId + "/");

		webSettings.setAnalyticsScriptData(googleAnalyticsScript);

		return webSettings;
	}

}
