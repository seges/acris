package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.node.NodeDefinition;

public class DescriptionPostProcessor extends AbstractMetaTagPostProcessor {

	protected boolean supportsNode(Node node, GeneratorEnvironment generatorEnvironment) {
		if (!super.supportsNode(node, generatorEnvironment)) {
			return false;
		}

		return (generatorEnvironment.getContent().getDescription() == null || 
				generatorEnvironment.getContent().getDescription().length() == 0);
	}

	@Override
	protected String getMetaTagName() {
		return NodeDefinition.DESCRIPTION_TAG_NAME.getName();
	}
}