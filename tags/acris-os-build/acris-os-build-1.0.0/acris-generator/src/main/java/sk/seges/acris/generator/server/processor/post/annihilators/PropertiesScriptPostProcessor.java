package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.ScriptTag;
import org.springframework.stereotype.Component;

@Component
public class PropertiesScriptPostProcessor extends AbstractPostProcessorAnnihilator {

	protected boolean supportsParent(Node node) {
		return (node instanceof HeadTag);	
	}

	protected boolean supportsNode(Node node) {
		if (node instanceof ScriptTag) {
			String jsSource = ((ScriptTag)node).getAttribute("src");
			return (jsSource != null && jsSource.toLowerCase().indexOf("properties.js") > 0); 
		}
		
		return false;
	}
}