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
}