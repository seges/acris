package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.BodyTag;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;

public class GoogleAnalyticPostProcessor extends AbstractElementPostProcessor {

	@Override
	public boolean supports(Node node) {
		return (node instanceof BodyTag); 
	}

	@Override
	public boolean process(Node node) {
		BodyTag body = (BodyTag)node;
		TextNode textTag = new TextNode(webSettings.getGoogleAnalyticsScript());
		body.getChildren().add(textTag);
		
		return true;
	}
}