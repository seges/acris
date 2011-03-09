package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;
import org.htmlparser.util.NodeList;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.node.NodeDefinition;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.AbstractMetaTagPostProcessor;

public abstract class AbstractMetaTagAppenderPostProcessor extends AbstractElementPostProcessor {

	@Override
	public synchronized boolean supports(Node node, GeneratorEnvironment generatorEnvironment) {
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