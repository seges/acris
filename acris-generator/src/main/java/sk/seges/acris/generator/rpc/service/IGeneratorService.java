/**
 * 
 */
package sk.seges.acris.generator.rpc.service;

import java.util.List;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.util.Tuple;

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

	String getOfflineContentHtml(String entryPointFileName, String header, String contentWrapper, String content, GeneratorToken token, String currentServerURL);

	/**
	 * File provider services
	 */
	Tuple<String, String> readHtmlBodyFromFile(String filename);

	void writeTextToFile(String content, GeneratorToken token);

	/**
	 * Content provider services
	 */
	List<String> getAvailableNiceurls(String lang, String webId);
}