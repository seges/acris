package sk.seges.acris.generator.server.processor;

import java.io.IOException;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Source;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class HTMLBodyProcessing {

	private static final String A_TAG_NAME = "a";
	private static final String BODY_TAG_NAME = "body";

	private final Parser parser;

	private NodeFilter anchorFilter = new TagNameFilter(A_TAG_NAME);
	private NodeFilter bodyFilter = new TagNameFilter(BODY_TAG_NAME);

	public HTMLBodyProcessing(final String content) {
		Lexer lexer = new Lexer(content);
		parser = new Parser(lexer);
	}

	private NodeList getAnchors() {
		NodeList nodes;

		parser.reset();

		try {
			nodes = parser.parse(anchorFilter);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}

		return nodes;
	}

	public String replaceAnchors(String protocol, String webId, String port) {
		NodeList anchors = getAnchors();
		
		int size = anchors.size();
		
		for (int i = 0; i < size; i++) {
			Node anchor = anchors.elementAt(i);
			
			if (anchor instanceof LinkTag) {
				if (((LinkTag)anchor).getLink().startsWith("#")) {
					((LinkTag)anchor).setLink(protocol + webId + (port.equals("80") ? "" : port) + "/" + ((LinkTag)anchor).getLink().substring(1));
				}
			}
		}

		if (size > 0) {
			Node node = anchors.elementAt(0);
			
			while (node.getParent() != null) {
				node = node.getParent();
			}
			
			return node.toHtml();
		}
		
		parser.reset();
		Source source = parser.getLexer().getPage().getSource();
		int available = source.available();
		try {
			char[] chars = new char[available];
			
			source.read(chars, 0, available);
			return String.valueOf(chars, 0, available);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	/*
	private void replaceLinks(final NodeList nodes, final String title) {
		TitleTag node = getTitleNode();

		if (node != null) {
			removeRecursive(nodes, node);
		}

		TitleTag tag = new TitleTag();
		TextNode textNode = new TextNode(title);
		NodeList nodeList = new NodeList();
		nodeList.add(textNode);
		tag.setChildren(nodeList);

		TagNode tagNode = new TagNode();
		
		PageAttribute pa = new PageAttribute();
		pa.setName("/" + ((Attribute) tag.getAttributesEx().get(0)).getName());
		pa.setValue(null);
		Vector<PageAttribute> vector = new Vector<PageAttribute>();
		vector.add(pa);
		tagNode.setAttributesEx(vector);
		// tagNode.setAttribute(pa);
		tag.setEndTag(tagNode);
		
		nodes.elementAt(0).getChildren().add(tag);
	}

	private void replaceMetaTag(final String tagName, final NodeList nodes, final String contentValue) {
		MetaTag node = getMetaTagNode(tagName);

		if (node != null) {
			removeRecursive(nodes, node);
		}

		addMetaTag(tagName, nodes, contentValue);
	}

	private int indexOf(NodeList nodes, Node searchNode) {
		MetaTag node;
		int loc = 0;

		final String searchNodeHtml = searchNode.toHtml();

		for (SimpleNodeIterator e = nodes.elements(); e.hasMoreNodes();) {
			Node currentNode = e.nextNode();

			if (currentNode instanceof MetaTag) {
				node = (MetaTag) currentNode;

				if (searchNode instanceof MetaTag) {
					String nodeName = node.getAttribute(NAME_ATTRIBUTE_NAME);
					String nodeContent = node.getAttribute(CONTENT_ATTRIBUTE_NAME);
					
					if (nodeName != null && nodeName.equalsIgnoreCase(((MetaTag) searchNode).getAttribute(NAME_ATTRIBUTE_NAME)) && 
						nodeContent != null && nodeContent.equalsIgnoreCase(((MetaTag) searchNode).getAttribute(CONTENT_ATTRIBUTE_NAME))) {
						return loc;
					}
				}
			} else {
				final String currentNodeHtml = currentNode.toHtml();

				if (currentNodeHtml.equalsIgnoreCase(searchNodeHtml)) {
					return loc;
				}
			}

			loc++;
		}
		return -1;
	}

	private HeadTag getHeadNode() {
		NodeList nodes;

		parser.reset();

		try {
			nodes = parser.parse(headFilter);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}

		if (nodes.size() == 0) {
			return null;
		}

		return (HeadTag) nodes.elementAt(0);
	}

	private MetaTag getMetaTagNode(String name) {

		if (name == null) {
			return null;
		}

		HeadTag headTag = getHeadNode();

		NodeList metaTags = headTag.searchFor(MetaTag.class, true);

		SimpleNodeIterator nodesIterator = metaTags.elements();

		while (nodesIterator.hasMoreNodes()) {
			MetaTag metaTag = (MetaTag) nodesIterator.nextNode();
			if (name.equalsIgnoreCase(metaTag.getAttribute(NAME_ATTRIBUTE_NAME))) {
				return metaTag;
			}
		}

		return null;
	}

	private TitleTag getTitleNode() {
		HeadTag headTag = getHeadNode();

		NodeList titleTags = headTag.searchFor(TitleTag.class, true);

		if (titleTags.size() == 0) {
			return null;
		}

		return (TitleTag) titleTags.elementAt(0);
	}*/
}