package sk.seges.acris.generator.server.processor;

import java.util.List;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;

public interface IContentInfoProvider {

	String findNiceurlForLanguage(String originalNiceUrl, String targetLanguage, String webId);
	
	String getContentKeywords(GeneratorToken token);
	
	String getContentDescription(GeneratorToken token);
	
	String getContentTitle(GeneratorToken token);
	
	boolean exists(GeneratorToken token);
	
	boolean isDefaultContent(GeneratorToken token);

	GeneratorToken getDefaultContent(String webId, String lang);

	List<String> getAvailableNiceurls(String lang, String webId);
}