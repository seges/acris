package sk.seges.acris.generator.server.processor.post.annihilators;


public class OnPropertyErrorFnAnnihilatorPostProcessor extends AbstractMetaTagNameAnnihilatorPostProcessor {

	private static final String ON_PROPERTY_ERROR_META_TAG_NAME = "gwt:onPropertyErrorFn";

	@Override
	protected String getMetaTagName() {
		return ON_PROPERTY_ERROR_META_TAG_NAME;
	}
}