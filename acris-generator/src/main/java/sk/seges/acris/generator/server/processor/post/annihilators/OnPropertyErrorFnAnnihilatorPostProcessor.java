package sk.seges.acris.generator.server.processor.post.annihilators;


import sk.seges.acris.generator.server.processor.post.TokenSupport;
import sk.seges.acris.site.client.json.params.WebParams.OfflineMode;

public class OnPropertyErrorFnAnnihilatorPostProcessor extends AbstractMetaTagNameAnnihilatorPostProcessor {

	private static final String ON_PROPERTY_ERROR_META_TAG_NAME = "gwt:onPropertyErrorFn";

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
		return ON_PROPERTY_ERROR_META_TAG_NAME;
	}
}