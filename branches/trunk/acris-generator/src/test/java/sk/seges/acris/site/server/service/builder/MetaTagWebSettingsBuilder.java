package sk.seges.acris.site.server.service.builder;

import java.util.HashSet;
import java.util.Set;

import sk.seges.acris.site.shared.domain.api.WebSettingsData;
import sk.seges.acris.site.shared.domain.api.WebSettingsData.MetaData;
import sk.seges.acris.site.shared.domain.api.WebSettingsData.MetaDataType;
import sk.seges.acris.site.shared.domain.dto.WebSettingsDTO;
import sk.seges.acris.site.shared.domain.dto.WebSettingsDTO.MetaDataDTO;

public class MetaTagWebSettingsBuilder implements IWebSettingsBuilder {

	@Override
	public WebSettingsData getWebSettings(String webId, Boolean localeSensitiveServer, String googleAnalyticsScript) {
		WebSettingsData webSettings = new WebSettingsDTO();
		webSettings.setWebId(webId);
		webSettings.setLanguage("en");

		Set<MetaData> metaData = new HashSet<MetaData>();
		
		MetaData robotsMetaData = new MetaDataDTO();
		robotsMetaData.setContent("NOINDEX");
		robotsMetaData.setType(MetaDataType.ROBOTS);
		metaData.add(robotsMetaData);

		MetaData authorMetaData = new MetaDataDTO();
		authorMetaData.setContent("Seges s.r.o.");
		authorMetaData.setType(MetaDataType.AUTHOR);
		metaData.add(authorMetaData);
		
		webSettings.setMetaData(metaData);
		
		webSettings.setTopLevelDomain("http://" + webId + "/");

		webSettings.setAnalyticsScriptData(googleAnalyticsScript);

		return webSettings;
	}

}