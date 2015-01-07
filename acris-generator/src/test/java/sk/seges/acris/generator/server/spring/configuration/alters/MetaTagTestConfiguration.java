package sk.seges.acris.generator.server.spring.configuration.alters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.MetaTagAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.MetaTagAppenderPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.OfflineSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;
import sk.seges.acris.site.server.domain.jpa.JpaWebSettings;
import sk.seges.acris.site.server.model.data.MetaDataData;
import sk.seges.acris.site.server.model.data.WebSettingsData;
import sk.seges.acris.site.shared.domain.api.MetaDataType;

import java.util.HashSet;
import java.util.Set;

@Import({WebSettingsServiceConfiguration.class, MockTestConfiguration.class, OfflineSettingsConfiguration.class})
public class MetaTagTestConfiguration {

    @Bean
    public WebSettingsData getWebSettings() {
        WebSettingsData webSettings = new JpaWebSettings();
        webSettings.setLanguage("en");

        Set<MetaDataData> metaData = new HashSet<MetaDataData>();

        MetaDataData robotsMetaData = new JpaWebSettings.JpaMetaData();
        robotsMetaData.setContent("NOINDEX");
        robotsMetaData.setType(MetaDataType.ROBOTS);
        metaData.add(robotsMetaData);

        MetaDataData authorMetaData = new JpaWebSettings.JpaMetaData();
        authorMetaData.setContent("Seges s.r.o.");
        authorMetaData.setType(MetaDataType.AUTHOR);
        metaData.add(authorMetaData);

        webSettings.setMetaData(metaData);

        webSettings.setAnalyticsScriptData(WebSettingsServiceConfiguration.MOCK_ANALYTICS_SCRIPT);

        return webSettings;
    }

    @Bean
	public AbstractElementPostProcessor metaTagPostProcessor() {
		return new MetaTagAlterPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor metaTagAppenderPostProcessor() {
		return new MetaTagAppenderPostProcessor();
	}
}