package sk.seges.acris.generator.server.processor.utils;

import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.manager.api.OfflineWebSettings;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.TokenSupport;

import java.util.HashSet;
import java.util.Set;

public class PostProcessorActivator {

    private OfflineClientWebParams.OfflineMode offlineMode = OfflineClientWebParams.OfflineMode.COMBINED;

	public PostProcessorActivator(OfflineWebSettings offlineWebSettings) {
        if (offlineWebSettings.getOfflineMode() != null) {
            //if there is no offline specified, use COMBINED as default
            this.offlineMode = offlineWebSettings.getOfflineMode();
        }
	}
	
	public boolean isActive(AbstractElementPostProcessor postProcessor, boolean index) {

        if (!postProcessor.getOfflineMode().contains(offlineMode)) {
            return false;
        }

        TokenSupport tokenSupport = index ? TokenSupport.DEFAULT_ONLY : TokenSupport.NON_DEFAULT;
        return postProcessor.getTokenSupport(offlineMode).contains(tokenSupport);
	}
}