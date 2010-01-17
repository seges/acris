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
	void saveContent(GeneratorToken token,
			String contentText, AsyncCallback<Boolean> callback);

	void getLastProcessingToken(AsyncCallback<GeneratorToken> callback);

	void getDomainForLanguage(String webId, String language, AsyncCallback<String> callback);

	void getOfflineContentHtml(String headerFilename, String content, GeneratorToken token, AsyncCallback<String> callback);

	/**
	 * File provider services
	 */
	void readTextFromFile(String filename, AsyncCallback<String> callback);

	void writeTextToFile(String content, GeneratorToken token, AsyncCallback<Void> callback);

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
	void getAvailableNiceurls(String language, String webId, AsyncCallback<List<String>> callback);
}