/**
 * 
 */
package sk.seges.acris.generator.rpc.service;

import java.util.List;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author fat
 */
public interface IGeneratorServiceAsync {
	/**
	 * Offline content generator service
	 */
	void saveContent(String token, String lang, String webId,
			String contentText, AsyncCallback<Boolean> callback);

	void getLastProcessingToken(AsyncCallback<GeneratorToken> callback);

	void getDomainForLanguage(String language, AsyncCallback<String> callback);

	/**
	 * File provider services
	 */
	void readTextFromFile(String filename, AsyncCallback<String> callback);

	void writeTextToFile(String headerFilename, String filename,
			String content, String token, String lang_country, String webId,
			AsyncCallback<Void> callback);

	/**
	 * Properties provider services
	 */
	void getVirtualServerName(AsyncCallback<String> callback);

	void getVirtualServerPort(AsyncCallback<Integer> callback);

	void getVirtualServerProtocol(AsyncCallback<String> callback);

	void isLocaleSensitiveServer(AsyncCallback<Boolean> callback);

	void getGoogleAnalyticsScript(AsyncCallback<String> callback);
	
	/**
	 * Content provider services
	 */
	void getAvailableTokens(String lang_country, String webId, AsyncCallback<List<String>> callback);
}