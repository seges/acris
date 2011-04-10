package sk.seges.acris.generator.server.processor.factory.api;

import org.htmlparser.Parser;

public interface ParserFactory {
	
	Parser createParser(String content);	
}