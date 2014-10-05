/**
 * 
 */
package sk.seges.acris.generator.shared.service;

import com.google.gwt.user.client.rpc.RemoteService;
import sk.seges.acris.common.util.Tuple;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.sesam.pap.service.annotation.RemoteServiceDefinition;
import sk.seges.sesam.shared.model.dto.PageDTO;

import java.util.ArrayList;

/**
 * @author Peter Simun (simun@seges.sk)
 */
@RemoteServiceDefinition
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
	ArrayList<String> getAvailableNiceurls(PageDTO page);
}