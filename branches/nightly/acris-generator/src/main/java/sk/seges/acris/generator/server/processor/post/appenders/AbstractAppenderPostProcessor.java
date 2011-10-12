package sk.seges.acris.generator.server.processor.post.appenders;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

public abstract class AbstractAppenderPostProcessor extends AbstractElementPostProcessor {

	@Override
	public Kind getKind() {
		return Kind.APPENDER;
	}
}