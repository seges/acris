package sk.seges.acris.generator.server.processor.factory;

import org.htmlparser.Parser;
import org.htmlparser.lexer.Lexer;
import sk.seges.acris.generator.server.processor.factory.api.NodeParserFactory;

public class DefaultNodeParserFactory implements NodeParserFactory {

	@Override
	public synchronized Parser createParser(String content) {
		Lexer lexer = new Lexer(content);
		return new Parser(lexer);
	}
}
