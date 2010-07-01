package sk.seges.acris.generator.server.processor.post.alters;

import sk.seges.acris.generator.server.processor.IContentInfoProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

public abstract class AbstractContentInfoPostProcessor extends AbstractElementPostProcessor {

	protected IContentInfoProvider contentInfoProvider;

	public void setContentInfoProvider(IContentInfoProvider contentInfoProvider) {
		this.contentInfoProvider = contentInfoProvider;
	}
}
