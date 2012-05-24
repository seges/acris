package sk.seges.acris.generator.server.processor.post.alters;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

public abstract class AbstractAlterPostProcessor extends AbstractElementPostProcessor {

	@Override
	public Kind getKind() {
		return Kind.ALTER;
	}

}
