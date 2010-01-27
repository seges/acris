package sk.seges.acris.generator.server.processor.post.alters;

import sk.seges.acris.generator.server.processor.ContentInfoProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

public abstract class AbstractContentInfoPostProcessor extends AbstractElementPostProcessor {

	protected ContentInfoProvider contentInfoProvider;

	public void setContentInfoProvider(ContentInfoProvider contentInfoProvider) {
		this.contentInfoProvider = contentInfoProvider;
	}
}
