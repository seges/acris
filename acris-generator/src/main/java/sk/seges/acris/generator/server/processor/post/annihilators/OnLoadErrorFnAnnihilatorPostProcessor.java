package sk.seges.acris.generator.server.processor.post.annihilators;


import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.server.processor.post.TokenSupport;

public class OnLoadErrorFnAnnihilatorPostProcessor extends AbstractMetaTagNameAnnihilatorPostProcessor {

	private static final String ON_LOAD_ERROR_META_TAG_NAME = "gwt:onLoadErrorFn";

    @Override
    public OfflineClientWebParams.OfflineMode getOfflineMode() {
        return OfflineClientWebParams.OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineClientWebParams.OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	protected String getMetaTagName() {
		return ON_LOAD_ERROR_META_TAG_NAME;
	}
}