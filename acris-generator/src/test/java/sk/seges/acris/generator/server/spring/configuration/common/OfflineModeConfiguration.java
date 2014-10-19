package sk.seges.acris.generator.server.spring.configuration.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.shared.params.OfflineParameterType;
import sk.seges.acris.site.server.domain.jpa.JpaWebSettings;
import sk.seges.acris.site.server.model.data.WebSettingsData;

import javax.annotation.PostConstruct;

/**
 * Created by PeterSimun on 18.10.2014.
 */
@Configuration
public class OfflineModeConfiguration {

    @Autowired
    private WebSettingsData webSettings;

    @PostConstruct
    public void getWebSettingsParams() {
        String parameters = "{\"" + OfflineParameterType.OFFLINE_MODE.getKey() + "\":\"" + OfflineClientWebParams.OfflineMode.OFFLINE.toString() + "\",\"offlineAutodetectMode\":false,\"publishOnSaveEnabled\":true}";
        webSettings.setParameters(parameters);
    }
}