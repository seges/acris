package sk.seges.acris.generator.server.processor.utils;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

import sk.seges.acris.generator.server.processor.node.NodeDefinition;
import sk.seges.acris.generator.server.processor.post.alters.AbstractMetaTagPostProcessor;

/**
 * Various utilities classes for processing the HTML nodes using HTML parser
 *
 * @author Peter Simun (simun@seges.sk)
 */
public class NodesUtils {

	/**
	 * Represents name attribute of the meta tag HTML node. This class is used for transforming
	 * meta tag node into the String representation - original implementation did not serialize
	 * node correctly and did not separate attributes with the space, so the result was
	 * <meta name="keywords"description=""/>
	 * 
	 * @author Peter Simun (simun@seges.sk)
	 */
	public static class MetaTagNameAttribute extends Attribute {

		private static final long serialVersionUID = 1804473692890789209L;

		public MetaTagNameAttribute(NodeDefinition node) {
			this(node.getName());
		}
		
		/**
		 * @param value Name of the meta tag
		 */
		public MetaTagNameAttribute(String value) {
			super(AbstractMetaTagPostProcessor.NAME_ATTRIBUTE_NAME, value);
		}
	}
	
	/**
	 * Sets title into {@link TitleTag}. From the HTML parser fundamentals node should
	 * contains a textNode which holds the plain text. So, there can be various situations
	 * when there is:
	 * <ul>
	 * 	<li>no {@link TextNode} in the {@link TitleTag} </li>
	 * 	<li>empty {@link TextNode} </li>
	 * </ul>
	 * in the {@link TitleTag}.
	 * 
	 * @param titleTag HTML title node which title is going to be changes
	 * @param title String representation of the HTML title (just plain text)
	 * @return {@link TitleTag} with the correct HTML representation and title text
	 */
	public static final TitleTag setTitle(TitleTag titleTag, String title) {

		if (title == null) {
			title = "";
		}

		TextNode titleText = null;

		if (titleTag.getChildren() == null) {
			titleTag.setChildren(new NodeList());
		}

		if (titleTag.getChildCount() == 0) {
			titleText = new TextNode(title);
			titleTag.getChildren().add(titleText);
		} else {
			titleTag.getChildren().elementAt(0).setText(title);
		}
		return titleTag;
	}

	@SuppressWarnings("unchecked")
	public static synchronized final <T extends Node> T getChildNode(Node parentNode, Class<T> clazz, Attribute... attributes) {
		if (parentNode == null) {
			return null;
		}

		NodeList nodes = parentNode.getChildren();
		
		if (nodes == null) {
			return null;
		}

		SimpleNodeIterator elements = nodes.elements();

		while (elements.hasMoreNodes()) {
			Node node = elements.nextNode();
			if (node.getClass().equals(clazz)) {
				if (attributes != null && node instanceof Tag) {
					for (Attribute attribute: attributes) {
						String attributeValue = getAttributeNoCase(((Tag)node), attribute.getName());
						if (attributeValue != null && attributeValue.toLowerCase().equals(attribute.getValue().toLowerCase())) {
							return (T)node;
						}
					}
				} else if (attributes == null) {
					return (T)node;
				}
			}
		}
		
		return null;
	}
	
	private static String getAttributeNoCase(Tag tag, String name) {
		for (Object attribute: tag.getAttributesEx()) {
			Attribute attr = (Attribute)attribute;
			
			if (attr.getName() == null) {
				continue;
			}
			
			if (attr.getName().trim().toLowerCase().equals(name.trim())) {
				return attr.getValue();
			}
		}
		
		return null;
	}
	
	public static final <T extends Node> T getChildNode(Node parentNode, Class<T> clazz) {
		return getChildNode(parentNode, clazz, (Attribute[])null);
	}
}
