package sk.seges.acris.generator.server.processor.post.annihilators;

import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;

public class EmulateIE7AnnihilatorPostProcessor extends AbstractMetaTagContentAnnihilatorPostProcessor {

	private static final String IE7_EMULATE_META_TAG_CONTENT = "IE=EmulateIE7";

    @Override
    public OfflineMode getOfflineMode() {
        return OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	protected String getMetaTagContent() {
		return IE7_EMULATE_META_TAG_CONTENT;
	}

}