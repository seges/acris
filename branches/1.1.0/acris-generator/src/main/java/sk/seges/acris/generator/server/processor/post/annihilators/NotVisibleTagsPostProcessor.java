package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.Tag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.utils.CSSStyleDetector;

public class NotVisibleTagsPostProcessor extends AbstractPostProcessorAnnihilator {

	@Override
	protected boolean supportsParent(Node node, GeneratorEnvironment generatorEnvironment) {
		return node != null && node.getChildren() != null;
	}

	@Override
	protected boolean supportsNode(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof Tag) {
			return !(new CSSStyleDetector((Tag)node).isVisible());
		}
		
		return false;
	}
}