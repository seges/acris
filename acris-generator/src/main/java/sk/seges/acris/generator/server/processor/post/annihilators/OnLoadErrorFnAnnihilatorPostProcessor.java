package sk.seges.acris.generator.server.processor.post.annihilators;


import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;

public class OnLoadErrorFnAnnihilatorPostProcessor extends AbstractMetaTagNameAnnihilatorPostProcessor {

	private static final String ON_LOAD_ERROR_META_TAG_NAME = "gwt:onLoadErrorFn";

    @Override
    public OfflineMode getOfflineMode() {
        return OfflineMode.BOTH;
    }

    @Override
    public TokenSupport getTokenSupport(OfflineMode offlineMode) {
        return TokenSupport.ALL;
    }

    @Override
	protected String getMetaTagName() {
		return ON_LOAD_ERROR_META_TAG_NAME;
	}
}