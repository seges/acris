package sk.seges.acris.generator.server.processor.post.appenders;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.lexer.PageAttribute;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.tags.MetaTag;

import sk.seges.acris.generator.server.processor.model.api.GeneratorEnvironment;
import sk.seges.acris.generator.server.processor.node.NodeDefinition;
import sk.seges.acris.generator.server.processor.post.alters.AbstractMetaTagAlterPostProcessor;
import sk.seges.acris.generator.server.processor.utils.NodesUtils;

public abstract class AbstractMetaTagAppenderPostProcessor extends AbstractAppenderPostProcessor {

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
		NodesUtils.appendChild(head, paddingNode);

		MetaTag metaTag = new MetaTag();
		metaTag.setTagName(metaTag.getTagName().toLowerCase());
		metaTag.getAttributesEx().add(new PageAttribute(" "));
		metaTag.getAttributesEx().add(new Attribute(AbstractMetaTagAlterPostProcessor.NAME_ATTRIBUTE_NAME, name, '"'));
		metaTag.getAttributesEx().add(new PageAttribute(" "));
		metaTag.getAttributesEx().add(new Attribute("content", content == null ? "" : content, '"'));

		metaTag.setEmptyXmlTag(true);

		NodesUtils.appendChild(head, metaTag);
	}
}