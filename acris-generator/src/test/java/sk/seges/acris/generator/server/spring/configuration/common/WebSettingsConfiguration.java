package sk.seges.acris.generator.server.spring.configuration.common;

import org.springframework.context.annotation.Bean;

import sk.seges.acris.site.server.domain.jpa.JpaWebSettings;
import sk.seges.acris.site.server.model.data.WebSettingsData;

/**
 * Created by PeterSimun on 18.10.2014.
 */
public class WebSettingsConfiguration {

    @Bean
    public WebSettingsData getWebSettings() {
        WebSettingsData webSettings = new JpaWebSettings();
        webSettings.setLanguage("en");
        webSettings.setAnalyticsScriptData(WebSettingsServiceConfiguration.MOCK_ANALYTICS_SCRIPT);
        return webSettings;
    }
}
