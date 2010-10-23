package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.ScriptTag;

import sk.seges.acris.site.shared.service.IWebSettingsService;

public class NochacheScriptPostProcessor extends AbstractPostProcessorAnnihilator {

	public NochacheScriptPostProcessor(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

	protected boolean supportsParent(Node node) {
		return (node instanceof HeadTag);	
	}

	protected boolean supportsNode(Node node) {
		if (node instanceof ScriptTag) {
			String jsSource = ((ScriptTag)node).getAttribute("src");
			return (jsSource != null && jsSource.toLowerCase().indexOf(".nocache.js") > 0); 
		}
		
		return false;
	}
}