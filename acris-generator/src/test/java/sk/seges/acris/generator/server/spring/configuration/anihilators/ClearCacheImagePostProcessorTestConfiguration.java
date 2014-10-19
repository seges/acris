package sk.seges.acris.generator.server.spring.configuration.anihilators;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.MockContentFactory;
import sk.seges.acris.generator.server.processor.MockContentInfoProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.ClearCacheImageAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.OfflineSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;
import sk.seges.acris.site.server.domain.api.ContentData;
import sk.seges.acris.site.server.domain.jpa.JpaWebSettings;
import sk.seges.acris.site.server.model.data.WebSettingsData;
import sk.seges.acris.site.shared.domain.mock.MockContent;

@Import({WebSettingsServiceConfiguration.class, WebSettingsConfiguration.class, OfflineSettingsConfiguration.class})
public class ClearCacheImagePostProcessorTestConfiguration {

	class MockLightContentFactory implements MockContentFactory {

		@Override
		public ContentData constructMockContent() {
			return new MockContent();
		}
	}


    @Bean
    public WebSettingsData getWebSettings() {
        WebSettingsData webSettings = new JpaWebSettings();
        //webSettings.setWebId(webId);
        webSettings.setLanguage("en");

        //webSettings.setTopLevelDomain("http://" + webId + "/");

        webSettings.setAnalyticsScriptData(WebSettingsServiceConfiguration.MOCK_ANALYTICS_SCRIPT);

        return webSettings;
    }

	@Bean
	public ContentDataProvider contentInfoProvider() {
		return new MockContentInfoProvider(new MockLightContentFactory());
	}

	@Bean
	public AbstractElementPostProcessor clearCacheImageAnnihilatorPostProcessor() {
		return new ClearCacheImageAnnihilatorPostProcessor();
	}
}
