package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;

import sk.seges.acris.site.shared.service.IWebSettingsService;

public abstract class AbstractMetaTagPostProcessor extends AbstractPostProcessorAnnihilator {

	protected AbstractMetaTagPostProcessor(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

	protected abstract String getMetaTagName();

	protected boolean supportsParent(Node node) {
		return (node instanceof HeadTag);
	}

	protected boolean supportsNode(Node node) {
		if (node instanceof MetaTag) {
			MetaTag metaTag = (MetaTag) node;
			return getMetaTagName().equals(metaTag.getMetaTagName());
		}

		return false;
	}
}