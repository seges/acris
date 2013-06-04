package sk.seges.acris.generator.server.processor;

import java.util.Vector;

import org.htmlparser.Attribute;
import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.filters.HasAttributeFilter;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.tags.CompositeTag;
import org.htmlparser.tags.HeadTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.stereotype.Component;

@Component
public class HTMLNodeSplitter {

	public HTMLNodeSplitter() {
	}

	private String getNullSafeBody(String content) {
		String body = getBody(content);
		return body == null ? content : body;
	}
	
	public String getBody(String content) {
		Node bodyNode = getTagByName(content, "body");
		if (bodyNode == null) {
			return null;
		}
		return bodyNode.getChildren().toHtml();
	}

	public String getHeaderText(String content) {
		Node headerNode = getTagByName(content, "head");
		if (headerNode == null) {
			return null;
		}
		return headerNode.getChildren().toHtml();
	}

	private String getHtmlFromNode(Node node) {
		Node topLevelNode = node;
		while (topLevelNode.getParent() != null) {
			topLevelNode = topLevelNode.getParent();
		}
		
		while (topLevelNode.getPreviousSibling() != null) {
			topLevelNode = topLevelNode.getPreviousSibling();
		}

		String result = "";
		
		do {
			result = result + topLevelNode.toHtml();
		} while (topLevelNode.getNextSibling() != null);
		
		return result;
	}

	private boolean nullsafeEqual(String value1, String value2) {
		if (value2 != null) {
			value2 = value2.toLowerCase();
		}

		return (value1 == null ? value2 == null : value1.toLowerCase().equals(value2));
	}
	
	private boolean nullsafeEqual(Attribute attribute1, Attribute attribute2) {
		if (!nullsafeEqual(attribute1.getName(), attribute2.getName())) {
			return false;
		}
		return nullsafeEqual(attribute1.getValue(), attribute2.getValue());
	}
	
	private boolean attributesEquals(Vector<Attribute> attributes1, Vector<Attribute> attributes2) {
		for (Attribute attribute1: attributes1) {
			boolean found = false;
			for (Attribute attribute2 : attributes2) {
				if (nullsafeEqual(attribute1, attribute2)) {
					found = true;
					break;
				}
			}
			
			if (!found) {
				return false;
			}
		}
		return true;
	}
	
	private boolean contains(NodeList nodes, Tag searchTag) {
		for (int i = 0; i < nodes.size(); i++) {
			Node node = nodes.elementAt(i);
			
			if (node instanceof Tag) {
				Tag tag = (Tag)node;
				if (tag.getTagName().toLowerCase().equals(searchTag.getTagName().toLowerCase())) {
					Vector tagAttributes = tag.getAttributesEx();
					Vector searchTagAttributes = searchTag.getAttributesEx();
					
					if (tagAttributes.size() == searchTagAttributes.size()) {
						if (attributesEquals(tagAttributes, searchTagAttributes)) {
							return true;
						}
					}
				}
			}
		}
		
		return false;
	}
	
	public String readDoctype(String html) {
		NodeIterator iterator = getNodeListIterator(html);
		try {
			if (iterator.hasMoreNodes()) {
				Node firstNode = iterator.nextNode();
				return firstNode.getText();
			}
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
		return null;
		
	}
	
	public String joinHeaders(String htmlContent, String header) {
		HeadTag htmlHeadTag = getTagByName(htmlContent, "head");
		HeadTag generatedHeadTag = getTagByName(header, "head");
		
		NodeIterator generatedHeadTagIterator = null;
		
		if (generatedHeadTag == null) {
			generatedHeadTagIterator = getNodeListIterator(header);
		} else {
			generatedHeadTagIterator = generatedHeadTag.elements();
		}
		
		NodeList htmlHeadChilds = htmlHeadTag.getChildren();

		
		try {
			while (generatedHeadTagIterator.hasMoreNodes()) {
				Node node = generatedHeadTagIterator.nextNode();
				
				if (node instanceof Tag) {
					if (!contains(htmlHeadChilds, (Tag)node)) {
						htmlHeadChilds.add(node);
					}
				}
			}
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
		
		return getHtmlFromNode(htmlHeadTag);
	}
	
	public String replaceHeader(String htmlContent, String header) {
		String headerText = getHeaderText(header);

		if (headerText != null) {
			header = headerText;
		}

		Node headNode = getTagByName(htmlContent, "head");

		if (headNode == null) {
			return null;
		}
		
		HeadTag headTag = (HeadTag)headNode;
		
		int size = headTag.getChildCount();
		for (int i = 0; i < size; i++) {
			headTag.removeChild(0);
		}
		TextNode headContent = new TextNode(header);
		headTag.getChildren().add(headContent);
		
		return getHtmlFromNode(headTag);
	}
	
	private <T extends CompositeTag> T removeTagChildren(T tag) {
		int size = tag.getChildCount();
		for (int i = 0; i < size; i++) {
			tag.removeChild(0);
		}
		return tag;
	}
	
	private <T extends CompositeTag> String setInnerHtml(T tag, String html) {
		TextNode bodyContent = new TextNode(html);
		NodeList children = tag.getChildren();
		
		if (children == null) {
			children = new NodeList();
			tag.setChildren(children);
		} 

		tag.getChildren().add(bodyContent);
		
		return getHtmlFromNode(tag);
	}

	public String replaceRootContent(String htmlContent, String rootContent) {
		rootContent = getNullSafeBody(rootContent);
		CompositeTag tag = getTagById(htmlContent, "rootContent");
		if (tag == null) {
			return replaceBody(htmlContent, rootContent);
		}
		
		removeTagChildren(tag);
		return setInnerHtml(tag, rootContent);
	}
	
	public String replaceBody(String htmlContent, String body) {
		
		String bodyText = getBody(body);
		
		if (bodyText != null) {
			body = bodyText;
		}
		
		Node bodyNode = getTagByName(htmlContent, "body");
		if (bodyNode == null) {
			return null;
		}
		BodyTag bodyTag = (BodyTag)bodyNode;
		
		int size = bodyTag.getChildCount();
		for (int i = 0; i < size; i++) {
			bodyTag.removeChild(0);
		}
		TextNode bodyContent = new TextNode(body);
		bodyTag.getChildren().add(bodyContent);
		
		return getHtmlFromNode(bodyTag);
	}
	
	
	@SuppressWarnings("unchecked")
	private <T extends CompositeTag> T getTagByName(String content, String tagName) {
		NodeFilter nodeFilter = new TagNameFilter(tagName);
		Lexer lexer = new Lexer(content);
		Parser parser = new Parser(lexer);
		NodeList nodes;
		try {
			nodes = parser.parse(nodeFilter);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}

		if (nodes == null) {
			return null;
		}
		
		Node node = nodes.elementAt(0);
		return (T)node;
	}

	@SuppressWarnings("unchecked")
	private NodeIterator getNodeListIterator(String content) {
		Lexer lexer = new Lexer(content);
		Parser parser = new Parser(lexer);
		try {
			return parser.elements();
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	private <T extends CompositeTag> T getTagById(String content, String id) {
		HasAttributeFilter nodeFilter = new HasAttributeFilter("id", id);
		Lexer lexer = new Lexer(content);
		Parser parser = new Parser(lexer);
		NodeList nodes;
		try {
			nodes = parser.parse(nodeFilter);
		} catch (ParserException e) {
			throw new RuntimeException(e);
		}

		if (nodes == null) {
			return null;
		}
		
		Node node = nodes.elementAt(0);
		return (T)node;
	}

}