package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.node.NodeDefinition;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;
import sk.seges.acris.generator.server.processor.utils.NodesUtils.MetaTagNameAttribute;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public class KeywordsMetaTagAppenderPostProcessor extends AbstractMetaTagAppenderPostProcessor {

	public KeywordsMetaTagAppenderPostProcessor(IWebSettingsService webSettingsService, ContentDataProvider contentMetaDataProvider) {
		super(webSettingsService, contentMetaDataProvider);
	}

	@Override
	public boolean supports(Node node) {
		return (node instanceof HeadTag);
	}

	@Override
	public boolean process(Node node) {
		if (NodesUtils.getChildNode(node, MetaTag.class, new MetaTagNameAttribute(NodeDefinition.KEYWORDS_TAG_NAME)) == null) {
			if (getContent().getKeywords() != null && getContent().getKeywords().length() > 0) {
				appendMetaTag((HeadTag) node, NodeDefinition.KEYWORDS_TAG_NAME, getContent().getKeywords());
			}
		}
		return true;
	}
}