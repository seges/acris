package sk.seges.acris.generator.server.processor.utils;

import sk.seges.acris.generator.server.manager.api.OfflineWebSettings;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;

public class PostProcessorActivator {

    private OfflineMode offlineMode = OfflineMode.COMBINED;

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