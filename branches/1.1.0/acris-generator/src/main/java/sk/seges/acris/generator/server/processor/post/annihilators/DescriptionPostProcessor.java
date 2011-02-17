package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.node.NodeDefinition;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public class DescriptionPostProcessor extends AbstractContentMetaTagPostProcessor {

	public DescriptionPostProcessor(IWebSettingsService webSettingsService, ContentDataProvider contentMetaDataProvider) {
		super(webSettingsService, contentMetaDataProvider);
	}

	protected boolean supportsNode(Node node) {
		if (!super.supportsNode(node)) {
			return false;
		}
		return (getContent().getDescription() == null || getContent().getDescription().length() == 0);
	}

	@Override
	protected String getMetaTagName() {
		return NodeDefinition.DESCRIPTION_TAG_NAME.getName();
	}

}