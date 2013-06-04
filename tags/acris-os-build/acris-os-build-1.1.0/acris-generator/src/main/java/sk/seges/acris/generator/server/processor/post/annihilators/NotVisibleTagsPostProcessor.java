package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.Tag;

import sk.seges.acris.generator.server.processor.utils.CSSStyleDetector;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public class NotVisibleTagsPostProcessor extends AbstractPostProcessorAnnihilator {

	public NotVisibleTagsPostProcessor(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

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