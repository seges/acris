package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.util.NodeList;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.node.NodeDefinition;
import sk.seges.acris.generator.server.processor.post.alters.AbstractContentMetaDataPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.AbstractMetaTagPostProcessor;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public abstract class AbstractMetaTagAppenderPostProcessor extends AbstractContentMetaDataPostProcessor {

	public AbstractMetaTagAppenderPostProcessor(IWebSettingsService webSettingsService, ContentDataProvider contentMetaDataProvider) {
		super(webSettingsService, contentMetaDataProvider);
	}

	@Override
	public boolean supports(Node node) {
		return (node instanceof HeadTag);
	}

	protected void appendMetaTag(HeadTag head, NodeDefinition node, String content) {
		appendMetaTag(head, node.getName(), content);
	}
	
	@SuppressWarnings("unchecked")
	protected void appendMetaTag(HeadTag head, String name, String content) {
		TextNode paddingNode = new TextNode("\t");
		appendChild(head, paddingNode);

		MetaTag metaTag = new MetaTag();
		metaTag.setTagName(metaTag.getTagName().toLowerCase());
		metaTag.getAttributesEx().add(new Attribute(" " + AbstractMetaTagPostProcessor.NAME_ATTRIBUTE_NAME, name, '"'));
		metaTag.getAttributesEx().add(new Attribute(" content", content == null ? "" : content, '"'));

		metaTag.setEmptyXmlTag(true);

		appendChild(head, metaTag);
	}

	private void appendChild(Node node, Node child) {
		NodeList nodes = node.getChildren();
		if (nodes == null) {
			node.setChildren(new NodeList());
		}
		node.getChildren().add(child);
	}
}
