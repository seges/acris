package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.HeadTag;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public class GoogleAnalyticPostProcessor extends AbstractElementPostProcessor {

	public GoogleAnalyticPostProcessor(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

	@Override
	public boolean supports(Node node) {
		return (node instanceof HeadTag); 
	}

	@Override
	public boolean process(Node node) {
		HeadTag head = (HeadTag)node;
		if (!validateAnalyticsData(webSettings.getAnalyticsScriptData())) {
			return false;
		}

		TextNode textTag = new TextNode(webSettings.getAnalyticsScriptData());
		head.getChildren().add(textTag);
		
		return true;
	}
	
	private boolean validateAnalyticsData(String analyticsScript) {
		return (analyticsScript != null && analyticsScript.length() > 0);
	}
}