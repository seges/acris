package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;
import org.htmlparser.nodes.TagNode;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.utils.CSSStyleClassDetector;

public abstract class AbstractStyleClassNameAlter extends AbstractAlterPostProcessor {

	protected abstract String getStyleClassName();
	
	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof TagNode) {
			return new CSSStyleClassDetector((TagNode)node).hasStyleClass(getStyleClassName());
		}
		
		return false;
	}

}