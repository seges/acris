package sk.seges.acris.site.server.service.builder;

import java.util.HashSet;
import java.util.Set;

import sk.seges.acris.site.server.domain.api.server.model.data.MetaDataData;
import sk.seges.acris.site.server.domain.api.server.model.data.WebSettingsData;
import sk.seges.acris.site.server.domain.jpa.JpaWebSettings;
import sk.seges.acris.site.server.domain.jpa.JpaWebSettings.JpaMetaData;
import sk.seges.acris.site.shared.domain.api.MetaDataType;

public class MetaTagWebSettingsBuilder implements IWebSettingsBuilder {

	@Override
	public WebSettingsData getWebSettings(String webId, Boolean localeSensitiveServer, String googleAnalyticsScript) {
		WebSettingsData webSettings = new JpaWebSettings();
		webSettings.setWebId(webId);
		webSettings.setLanguage("en");

		Set<MetaDataData> metaData = new HashSet<MetaDataData>();
		
		MetaDataData robotsMetaData = new JpaMetaData();
		robotsMetaData.setContent("NOINDEX");
		robotsMetaData.setType(MetaDataType.ROBOTS);
		metaData.add(robotsMetaData);

		MetaDataData authorMetaData = new JpaMetaData();
		authorMetaData.setContent("Seges s.r.o.");
		authorMetaData.setType(MetaDataType.AUTHOR);
		metaData.add(authorMetaData);
		
		webSettings.setMetaData(metaData);
		
		webSettings.setTopLevelDomain("http://" + webId + "/");

		webSettings.setAnalyticsScriptData(googleAnalyticsScript);

		return webSettings;
	}

}