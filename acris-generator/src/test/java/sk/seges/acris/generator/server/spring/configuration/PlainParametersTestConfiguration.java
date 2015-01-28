package sk.seges.acris.generator.server.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.generator.server.processor.factory.CommaSeparatedParameterManagerFactory;
import sk.seges.acris.generator.server.processor.factory.JSONOfflineWebSettingsFactory;
import sk.seges.acris.generator.server.processor.factory.PostProcessorActivatorFactory;
import sk.seges.acris.generator.server.processor.factory.api.OfflineWebSettingsFactory;
import sk.seges.acris.generator.server.processor.factory.api.ParametersManagerFactory;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.DescriptionMetaTagAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.DescriptionMetaTagAppenderPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;
import sk.seges.acris.generator.shared.params.OfflineParameterType;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;
import sk.seges.acris.site.server.domain.jpa.JpaWebSettings;
import sk.seges.acris.site.server.model.data.WebSettingsData;

@Import({WebSettingsServiceConfiguration.class, MockTestConfiguration.class})
public class PlainParametersTestConfiguration {

    @Bean
    public ParametersManagerFactory parametersManagerFactory() {
        return new CommaSeparatedParameterManagerFactory();
    }

    @Bean
    public WebSettingsData getWebSettings() {
        WebSettingsData webSettings = new JpaWebSettings();
        webSettings.setParameters(OfflineParameterType.OFFLINE_MODE.getKey() + "=" + OfflineMode.COMBINED.toString() + ";");
        webSettings.setLanguage("en");
        webSettings.setAnalyticsScriptData(WebSettingsServiceConfiguration.MOCK_ANALYTICS_SCRIPT);
        return webSettings;
    }

    @Bean
    public OfflineWebSettingsFactory offlineWebSettingsFactory(ParametersManagerFactory parametersManagerFactory) {
        return new JSONOfflineWebSettingsFactory(parametersManagerFactory);
    }

    @Bean
    public PostProcessorActivatorFactory postProcessorActivatorFactory(OfflineWebSettingsFactory offlineWebSettingsFactory) {
        return new PostProcessorActivatorFactory(offlineWebSettingsFactory);
    }

	@Bean
	public AbstractElementPostProcessor descriptionMetaTagPostProcessor() {
		return new DescriptionMetaTagAlterPostProcessor();
	}
	
	@Bean
	public AbstractElementPostProcessor descriptionMetaTagAppenderPostProcessor() {
		return new DescriptionMetaTagAppenderPostProcessor();
	}
}