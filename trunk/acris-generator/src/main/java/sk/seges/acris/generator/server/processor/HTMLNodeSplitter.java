package sk.seges.acris.generator.server.processor;

import org.htmlparser.Node;
import org.htmlparser.NodeFilter;
import org.htmlparser.Parser;
import org.htmlparser.filters.TagNameFilter;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.BodyTag;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.springframework.stereotype.Component;

@Component
public class HTMLNodeSplitter {

	public HTMLNodeSplitter() {
	}

	public String getBody(String content) {
		Node bodyNode = getTagInnerHtml(content, "body");
		if (bodyNode == null) {
			return null;
		}
		return bodyNode.getChildren().toHtml();
	}

	public String replaceBody(String htmlContent, String body) {
		
		String bodyText = getBody(body);
		
		if (bodyText != null) {
			body = bodyText;
		}
		
		Node bodyNode = getTagInnerHtml(htmlContent, "body");
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
		
		Node topLevelNode = bodyTag;
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
	
	private Node getTagInnerHtml(String content, String tagName) {
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
		
		return nodes.elementAt(0);
	}

}