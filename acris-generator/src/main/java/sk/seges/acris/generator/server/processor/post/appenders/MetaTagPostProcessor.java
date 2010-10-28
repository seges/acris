package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.util.NodeList;

import sk.seges.acris.generator.server.processor.post.alters.AbstractContentInfoPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.AbstractMetaTagPostProcessor;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;
import sk.seges.acris.generator.server.processor.utils.NodesUtils.MetaTagNameAttribute;
import sk.seges.acris.site.shared.domain.api.WebSettingsData.MetaData;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public class MetaTagPostProcessor extends AbstractContentInfoPostProcessor {

	public MetaTagPostProcessor(IWebSettingsService webSettingsService) {
		super(webSettingsService);
	}

	@Override
	public boolean supports(Node node) {
		return (node instanceof HeadTag);
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean process(Node node) {
		if (webSettings.getMetaData() == null) {
			return true;
		}

		for (MetaData metaData : webSettings.getMetaData()) {

			MetaTag metaTag = NodesUtils.getChildNode(node, MetaTag.class, new MetaTagNameAttribute(metaData.getType().getName()));
			if (metaTag == null) {
				TextNode paddingNode = new TextNode("\t");
				appendChild(node, paddingNode);

				metaTag = new MetaTag();
				metaTag.getAttributesEx().add(new Attribute(" " + AbstractMetaTagPostProcessor.NAME_ATTRIBUTE_NAME, metaData.getType().getName(), '"'));
				String content = metaData.getContent();
				metaTag.getAttributesEx().add(new Attribute (" content", content == null ? "" : content, '"'));

				metaTag.setEmptyXmlTag(true);
				appendChild(node, metaTag);
			}
		}

		return true;
	}

	private void appendChild(Node node, Node child) {
		NodeList nodes = node.getChildren();
		if (nodes == null) {
			node.setChildren(new NodeList());
		}
		node.getChildren().add(child);
	}
}