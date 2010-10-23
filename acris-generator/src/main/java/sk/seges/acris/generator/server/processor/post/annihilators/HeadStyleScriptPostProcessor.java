package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.StyleTag;

import sk.seges.acris.site.shared.service.IWebSettingsService;

public class HeadStyleScriptPostProcessor extends AbstractPostProcessorAnnihilator {

	public HeadStyleScriptPostProcessor(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

	protected boolean supportsParent(Node node) {
		return (node instanceof HeadTag);	
	}

	protected boolean supportsNode(Node node) {
		return (node instanceof StyleTag);
	}
}