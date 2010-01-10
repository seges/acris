package sk.seges.acris.generator.client;

import sk.seges.acris.generator.rpc.service.IGeneratorServiceAsync;
import sk.seges.acris.util.URLUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class ContentProcessor {

	/**
	 * Generator properties
	 */
	private GeneratorProperties generatorProperties;
	
	/**
	 * Generator service
	 */
	private IGeneratorServiceAsync generatorService;

	public ContentProcessor(GeneratorProperties generatorProperties, IGeneratorServiceAsync generatorService) {
		this.generatorProperties = generatorProperties;
		this.generatorService = generatorService;
	}

	public void processContent(String content, String webId, String lang, final AsyncCallback<String> callback) {
		
		//obtaining current server URL, like: http://192.168.1.21:3290/
		final String currentServerURL = GWT.getHostPageBaseURL().replaceAll(GWT.getModuleName() + "/", "");

		//remove history frame - used only for GWT needs, not for offline content purposes
		//TODO - do it better. Remove style and use regexp
		final String modifiedContent = content
				.replaceAll(
						"<IFRAME id=__gwt_historyFrame style=\"BORDER-TOP-WIDTH: 0px; BORDER-LEFT-WIDTH: 0px; BORDER-BOTTOM-WIDTH: 0px; WIDTH: 0px; HEIGHT: 0px; BORDER-RIGHT-WIDTH: 0px\" src=\"javascript:''\"></IFRAME>","");

		String virtualServer = generatorProperties.getVirtualServerProtocol() + 
									generatorProperties.getVirtualServerName();

		//add port to the server definition
		//Only for port different than 80 (it does not make sense use definition like www.seges.sk:80 (its the same as www.seges.sk)
		if (generatorProperties.getVirtualServerPort() != null && generatorProperties.getVirtualServerPort() != 80) {
			virtualServer = virtualServer + ":" + generatorProperties.getVirtualServerPort();
		}

		final String virtualServerName = virtualServer;
			
		generatorService.getDomainForLanguage(webId, lang, new AsyncCallback<String>() {
			
			public void onSuccess(String domain) {
				String content = modifiedContent;

				if (generatorProperties.isLocaleSensitiveServer()) {
					content = content.replaceAll(currentServerURL, URLUtils
							.getLocalizedServerHost(virtualServerName, domain) + "/");
				} else {
					content = content.replaceAll(currentServerURL, virtualServerName + "/");
				}

				if (generatorProperties.isLocaleSensitiveServer()) {
					content = content.replaceAll(currentServerURL, URLUtils
							.getLocalizedServerHost(virtualServerName, domain));
				} else {
					content = content.replaceAll(currentServerURL, virtualServerName);
				}

				content = content.replaceAll(GWT.getModuleName() + "/", "");
				
				callback.onSuccess(content);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
		});
	}
}