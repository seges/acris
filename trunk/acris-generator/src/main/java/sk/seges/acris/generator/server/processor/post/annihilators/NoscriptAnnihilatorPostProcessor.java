package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.tags.BodyTag;

import sk.seges.acris.generator.server.processor.htmltags.NoScriptTag;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;

public class NoscriptAnnihilatorPostProcessor extends AbstractAnnihilatorPostProcessor {

	@Override
	protected boolean supportsParent(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof BodyTag);	
	}

	@Override
	protected boolean supportsNode(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof NoScriptTag);
	}

}