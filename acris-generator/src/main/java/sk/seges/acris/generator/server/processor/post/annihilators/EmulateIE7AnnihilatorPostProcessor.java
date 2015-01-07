package sk.seges.acris.generator.server.processor.post.annihilators;

import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.processor.post.TokenSupport;

public class EmulateIE7AnnihilatorPostProcessor extends AbstractMetaTagContentAnnihilatorPostProcessor {

	private static final String IE7_EMULATE_META_TAG_CONTENT = "IE=EmulateIE7";

    @Override
    public OfflineClientWebParams.OfflineMode getOfflineMode() {
        return OfflineClientWebParams.OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineClientWebParams.OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	protected String getMetaTagContent() {
		return IE7_EMULATE_META_TAG_CONTENT;
	}

}