package sk.seges.acris.generator.server.processor.post.alters;

import org.springframework.stereotype.Component;

@Component
public class DescriptionMetaTagPostProcessor extends AbstractMetaTagPostProcessor {

	private static final String DESCRIPTION_TAG_NAME = "description";

	@Override
	protected String getMetaTagName() {
		return DESCRIPTION_TAG_NAME;
	}

	@Override
	protected String getMetaTagContent() {
		return contentInfoProvider.getContentDescription(generatorToken);
	}
}