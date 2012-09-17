package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;

public abstract class AbstractStyleClassNameAnnihilator extends AbstractAnnihilatorPostProcessor {
	
	@Override
	protected boolean supportsParent(Node node, GeneratorEnvironment generatorEnvironment) {
		return true;
	}

	protected abstract String getStyleClassName();
	
	@Override
	protected boolean supportsNode(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof TagNode) {
			String styleClass = ((TagNode)node).getAttribute(CLASS_ATTRIBUTE_NAME);
			return (styleClass != null && styleClass.toLowerCase().equals(getStyleClassName()));
		}
		
		return false;
	}
}