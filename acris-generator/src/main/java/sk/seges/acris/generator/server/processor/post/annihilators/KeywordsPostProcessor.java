package sk.seges.acris.generator.server.processor.post.annihilators;

import org.htmlparser.Node;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.node.NodeDefinition;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public class KeywordsPostProcessor extends AbstractContentMetaTagPostProcessor {

	public KeywordsPostProcessor(IWebSettingsService webSettingsService, ContentDataProvider contentMetaDataProvider) {
		super(webSettingsService, contentMetaDataProvider);
	}

	protected boolean supportsNode(Node node) {
		if (!super.supportsNode(node)) {
			return false;
		}
		return (getContent().getKeywords() == null || getContent().getKeywords().length() == 0);
	}
	
	@Override
	protected String getMetaTagName() {
		return NodeDefinition.KEYWORDS_TAG_NAME.getName();
	}		
}