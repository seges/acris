package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.ScriptTag;
import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;

public class JavascriptAnnihilatorPostProcessor extends AbstractAnnihilatorPostProcessor {

	@Override
	protected boolean supportsParent(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof HeadTag);	
	}

	@Override
	protected boolean supportsNode(Node node, GeneratorEnvironment generatorEnvironment) {
		if (node instanceof ScriptTag) {
			String jsSource = ((ScriptTag)node).getAttribute("src");
			return (jsSource == null || (!jsSource.toLowerCase().contains(".nocache.js") && !jsSource.toLowerCase().contains("properties.js")));
		}
		
		return false;
	}
}