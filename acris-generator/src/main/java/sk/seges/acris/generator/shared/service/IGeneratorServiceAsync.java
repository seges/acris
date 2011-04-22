/**
 * 
 */
package sk.seges.acris.generator.shared.service;

import java.util.ArrayList;

import sk.seges.acris.common.util.Tuple;
import sk.seges.acris.generator.shared.domain.GeneratorToken;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * @author Peter Simun
 */
public interface IGeneratorServiceAsync {

	/**
	 * Offline content generator service
	 */
	void getLastProcessingToken(AsyncCallback<GeneratorToken> callback);

	void writeOfflineContentHtml(String entryPointFileName, String header, String contentWrapper, String content, GeneratorToken token, String currentServerURL,
			AsyncCallback<Void> callback);

	/**
	 * File provider services
	 */
	void readHtmlBodyFromFile(String filename, AsyncCallback<Tuple<String, String>> callback);

	/**
	 * Content provider services
	 */
	void getAvailableNiceurls(String language, String webId, AsyncCallback<ArrayList<String>> callback);
}