/**
 * 
 */
package sk.seges.acris.generator.shared.service;

import java.util.ArrayList;

import sk.seges.acris.common.util.Tuple;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.sesam.dao.Page;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * @author Peter Simun (simun@seges.sk)
 */
public interface IGeneratorService extends RemoteService {

	/**
	 * Offline content generator service
	 */
	GeneratorToken getDefaultGeneratorToken(String language, String webId);

	void writeOfflineContentHtml(String entryPointFileName, String header, String contentWrapper, String content, GeneratorToken token, String currentServerURL);

	/**
	 * File provider services
	 */
	Tuple<String, String> readHtmlBodyFromFile(String filename);

	/**
	 * Content provider services
	 */
	ArrayList<String> getAvailableNiceurls(Page page);
}