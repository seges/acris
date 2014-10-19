package sk.seges.acris.generator.server.service;

import sk.seges.acris.common.util.Tuple;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.sesam.dao.Page;
import sk.seges.sesam.pap.service.annotation.LocalServiceDefinition;

import javax.annotation.Generated;
import java.util.ArrayList;

@LocalServiceDefinition(remoteService = sk.seges.acris.generator.shared.service.IGeneratorService.class)
@Generated(value = "sk.seges.corpis.appscaffold.model.pap.LocalServiceDataInterfaceProcessor")
public interface IGeneratorServiceLocal {
	 
	 
	ArrayList<String> getAvailableNiceurls(Page page);
	
	GeneratorToken getDefaultGeneratorToken(String language, String webId);
	
	Tuple<String, String> readHtmlBodyFromFile(String filename);
	
	void writeOfflineContentHtml(String entryPointFileName, String header, String contentWrapper, String content, GeneratorToken token, String currentServerURL, String defaultLocale);
}
