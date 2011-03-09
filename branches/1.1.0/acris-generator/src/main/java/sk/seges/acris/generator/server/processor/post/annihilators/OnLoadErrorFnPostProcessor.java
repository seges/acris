package sk.seges.acris.generator.server.processor.post.annihilators;


public class OnLoadErrorFnPostProcessor extends AbstractMetaTagPostProcessor {

	private static final String ON_LOAD_ERROR_META_TAG_NAME = "gwt:onLoadErrorFn";
	
	@Override
	protected String getMetaTagName() {
		return ON_LOAD_ERROR_META_TAG_NAME;
	}
}