package sk.seges.acris.generator.server.processor.utils;

import org.htmlparser.Node;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.SimpleNodeIterator;

public class NodesUtils {

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

	public static final Node getChildNode(Node parentNode, Class<? extends Node> clazz) {
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
				return node;
			}
		}
		return null;
	}
}
