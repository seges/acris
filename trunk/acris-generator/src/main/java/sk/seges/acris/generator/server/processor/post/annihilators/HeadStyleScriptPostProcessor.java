package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.StyleTag;
import org.springframework.stereotype.Component;

@Component
public class HeadStyleScriptPostProcessor extends AbstractPostProcessorAnnihilator {

	protected boolean supportsParent(Node node) {
		return (node instanceof HeadTag);	
	}

	protected boolean supportsNode(Node node) {
		return (node instanceof StyleTag);
	}
}