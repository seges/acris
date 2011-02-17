package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Node;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.node.NodeDefinition;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;
import sk.seges.acris.generator.server.processor.utils.NodesUtils.MetaTagNameAttribute;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public class DescriptionMetaTagAppenderPostProcessor extends AbstractMetaTagAppenderPostProcessor {

	public DescriptionMetaTagAppenderPostProcessor(IWebSettingsService webSettingsService, ContentDataProvider contentMetaDataProvider) {
		super(webSettingsService, contentMetaDataProvider);
	}

	@Override
	public boolean supports(Node node) {
		return (node instanceof HeadTag);
	}

	@Override
	public boolean process(Node node) {
		if (NodesUtils.getChildNode(node, MetaTag.class, new MetaTagNameAttribute(NodeDefinition.DESCRIPTION_TAG_NAME)) == null) {
			if (getContent().getDescription() != null && getContent().getDescription().length() > 0) {
				appendMetaTag((HeadTag) node, NodeDefinition.DESCRIPTION_TAG_NAME, getContent().getDescription());
			}
		}
		return true;
	}
}