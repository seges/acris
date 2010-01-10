/**
 * 
 */
package sk.seges.acris.generator.rpc.service;

import java.util.List;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author fat
 */
public interface IGeneratorService extends RemoteService {

	/**
	 * Offline content generator service
	 */
	boolean saveContent(GeneratorToken token, String contentText);

	GeneratorToken getLastProcessingToken();

	String getDomainForLanguage(String webId, String language);

	/**
	 * File provider services
	 */
	String readTextFromFile(String filename);

	void writeTextToFile(String headerFilename, String content, GeneratorToken token);

	/**
	 * Properties provider services
	 */
	String getVirtualServerName();

	Integer getVirtualServerPort();

	String getVirtualServerProtocol();

	Boolean isLocaleSensitiveServer();

	String getGoogleAnalyticsScript();

	/**
	 * Content provider services
	 */
	List<String> getAvailableTokens(String lang, String webId);
}