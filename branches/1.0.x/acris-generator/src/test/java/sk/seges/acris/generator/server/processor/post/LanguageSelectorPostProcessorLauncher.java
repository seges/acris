package sk.seges.acris.generator.server.processor.post;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.lexer.Lexer;
import org.htmlparser.lexer.Page;
import org.htmlparser.util.DefaultParserFeedback;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;

public class LanguageSelectorPostProcessorLauncher {
	protected Parser parser;
	protected Node node[];
	protected int nodeCount;
	protected Lexer mLexer;

	protected void createParser(String inputHTML) {
		mLexer = new Lexer(new Page(inputHTML));
		parser = new Parser(mLexer, new DefaultParserFeedback(
				DefaultParserFeedback.QUIET));
		node = new Node[40];
	}

	public void parseNodes() throws ParserException {
		nodeCount = 0;
		for (NodeIterator e = parser.elements(); e.hasMoreNodes();) {
			node[nodeCount++] = e.nextNode();
		}
	}

	public void process() {
		createParser("<a href=\"ssss\">test</a>");
		try {
			parseNodes();
		} catch (ParserException e) {
			e.printStackTrace();
		}
		
		int a=  0;
	}

	public static void main(String[] args) {
		LanguageSelectorPostProcessorLauncher languageSelectorPostProcessorTest = new LanguageSelectorPostProcessorLauncher();
		languageSelectorPostProcessorTest.process();
	}
}
