/**
 * 
 */
package sk.seges.acris.generator.shared.service;

import java.util.List;

import sk.seges.acris.common.util.Tuple;
import sk.seges.acris.generator.shared.domain.GeneratorToken;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Peter Simun (simun@seges.sk)
 */
public interface IGeneratorService extends RemoteService {

	/**
	 * Offline content generator service
	 */
	GeneratorToken getLastProcessingToken();

	void writeOfflineContentHtml(String entryPointFileName, String header, String contentWrapper, String content, GeneratorToken token, String currentServerURL);

	/**
	 * File provider services
	 */
	Tuple<String, String> readHtmlBodyFromFile(String filename);

	/**
	 * Content provider services
	 */
	List<String> getAvailableNiceurls(String lang, String webId);
}