package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.springframework.stereotype.Component;

import sk.seges.acris.generator.server.processor.utils.CSSStyleDetector;

@Component
public class NotVisibleTagsPostProcessor extends AbstractPostProcessorAnnihilator {

	@Override
	protected boolean supportsParent(Node node) {
		return node != null && node.getChildren() != null;
	}

	@Override
	protected boolean supportsNode(Node node) {
		if (node instanceof Tag) {
			return !(new CSSStyleDetector((Tag)node).isVisible());
		}
		
		return false;
	}
}