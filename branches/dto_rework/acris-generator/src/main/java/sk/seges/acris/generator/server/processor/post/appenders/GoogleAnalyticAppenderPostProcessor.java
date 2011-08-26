package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.HeadTag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;

public class GoogleAnalyticAppenderPostProcessor extends AbstractAppenderPostProcessor {

	@Override
	public boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
		return (node instanceof HeadTag); 
	}

	@Override
	public boolean process(Node node, GeneratorEnvironment generatorEnvironment) {
		HeadTag head = (HeadTag)node;
		if (!validateAnalyticsData(generatorEnvironment.getWebSettings().getAnalyticsScriptData())) {
			return false;
		}

		TextNode textTag = new TextNode(generatorEnvironment.getWebSettings().getAnalyticsScriptData());
		head.getChildren().add(textTag);
		
		return true;
	}
	
	private boolean validateAnalyticsData(String analyticsScript) {
		return (analyticsScript != null && analyticsScript.length() > 0);
	}
}