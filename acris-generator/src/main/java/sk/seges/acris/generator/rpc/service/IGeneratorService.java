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
	boolean saveContent(String token, String lang_country, String webId,
			String contentText);

	GeneratorToken getLastProcessingToken();

	String getDomainForLanguage(String language);

	/**
	 * File provider services
	 */
	String readTextFromFile(String filename);

	void writeTextToFile(String headerFilename, String filename,
			String content, String token, String lang_country, String webId);

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
	List<String> getAvailableTokens(String lang_country, String webId);
}