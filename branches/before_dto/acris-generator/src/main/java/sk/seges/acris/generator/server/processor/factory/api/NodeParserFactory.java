package sk.seges.acris.generator.server.processor.factory.api;

import org.htmlparser.Parser;

public interface NodeParserFactory {
	
	Parser createParser(String content);	
}