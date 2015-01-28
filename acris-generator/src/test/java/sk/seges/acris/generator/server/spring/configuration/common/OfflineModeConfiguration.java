package sk.seges.acris.generator.server.spring.configuration.common;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import sk.seges.acris.generator.shared.params.OfflineParameterType;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;
import sk.seges.acris.site.server.model.data.WebSettingsData;

/**
 * Created by PeterSimun on 18.10.2014.
 */
@Configuration
public class OfflineModeConfiguration {

    @Autowired
    private WebSettingsData webSettings;

    @PostConstruct
    public void getWebSettingsParams() {
        String parameters = "{\"" + OfflineParameterType.OFFLINE_MODE.getKey() + "\":\"" + OfflineMode.OFFLINE.toString() + "\",\"offlineAutodetectMode\":false,\"publishOnSaveEnabled\":true}";
        webSettings.setParameters(parameters);
    }
}