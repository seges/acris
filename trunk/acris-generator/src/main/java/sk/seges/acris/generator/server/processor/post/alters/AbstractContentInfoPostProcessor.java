package sk.seges.acris.generator.server.processor.post.alters;

import sk.seges.acris.generator.server.processor.DummyContentInfoProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

public abstract class AbstractContentInfoPostProcessor extends AbstractElementPostProcessor {

	protected DummyContentInfoProvider contentInfoProvider;

	public void setContentInfoProvider(DummyContentInfoProvider contentInfoProvider) {
		this.contentInfoProvider = contentInfoProvider;
	}
}
