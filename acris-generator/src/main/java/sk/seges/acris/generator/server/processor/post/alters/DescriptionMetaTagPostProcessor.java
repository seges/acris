package sk.seges.acris.generator.server.processor.post.alters;

import org.htmlparser.Node;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.node.NodeDefinition;
import sk.seges.acris.site.shared.service.IWebSettingsService;


public class DescriptionMetaTagPostProcessor extends AbstractMetaTagPostProcessor {

	public DescriptionMetaTagPostProcessor(IWebSettingsService webSettingsService, ContentDataProvider contentInfoProvider) {
		super(webSettingsService, contentInfoProvider);
	}

	@Override
	protected String getMetaTagName() {
		return NodeDefinition.DESCRIPTION_TAG_NAME.getName();
	}

	@Override
	protected String getMetaTagContent() {
		return getContent().getDescription();
	}

	@Override
	public boolean supports(Node node) {
		if (!super.supports(node)) {
			return false;
		}
		return (getContent().getDescription() != null && getContent().getDescription().length() > 0);
	}
}