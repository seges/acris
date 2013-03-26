package sk.seges.acris.generator.server.processor.post.annihilators;

public class EmulateIE7AnnihilatorPostProcessor extends AbstractMetaTagContentAnnihilatorPostProcessor {

	private static final String IE7_EMULATE_META_TAG_CONTENT = "IE=EmulateIE7";
	
	@Override
	protected String getMetaTagContent() {
		return IE7_EMULATE_META_TAG_CONTENT;
	}

}