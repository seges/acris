package sk.seges.acris.generator.client;

import sk.seges.acris.generator.rpc.service.IGeneratorServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Loader for maintaining IO operations, e.g. loading entry point, saving
 * generated content
 * 
 * @author fat
 */
public class OfflineContentProvider {

	private IGeneratorServiceAsync generatorService;

	private String initialContentFilename;
	
	public OfflineContentProvider(String moduleName, IGeneratorServiceAsync generatorService) {
		
		this.generatorService = generatorService;
		
		int lastDotIndex = moduleName.lastIndexOf(".");

		String pageName;

		if (lastDotIndex == -1) {
			return;
		}

		pageName = moduleName.substring(lastDotIndex + 1);
		moduleName = moduleName.substring(0, lastDotIndex);

		initialContentFilename = GWT.getModuleBaseURL() + pageName + ".html";
	}

	public void getEntryPointHTML(final AsyncCallback<String> callback) {

		generatorService.readTextFromFile(initialContentFilename,
				new AsyncCallback<String>() {

					public void onFailure(Throwable caught) {
						GWT.log(
								"Unable to read text from file. Please check entry html file: "
										+ initialContentFilename
										+ " and also RPC server side", caught);
						callback.onFailure(caught);
					}

					public void onSuccess(String result) {
						if (result == null) {
							GWT.log(
									"Unable to load default content. Please check entry html file: "
											+ initialContentFilename, null);
							callback.onFailure(new RuntimeException(
									"Unable to load default content. Please check entry html file: "
											+ initialContentFilename));
						} else {
							callback.onSuccess(result);
						}
					}
				});
	}

	public void saveContent(String content, String token, String lang_country,
			String webId, final AsyncCallback<Void> callback) {
		
		generatorService.writeTextToFile(initialContentFilename, token + "_"
				+ lang_country + ".html", content, token, lang_country, webId,
				new AsyncCallback<Void>() {

					public void onFailure(Throwable caught) {
						GWT.log("Unable to write text to the file. ", caught);
						callback.onFailure(caught);
					}

					public void onSuccess(Void result) {
						callback.onSuccess(result);
					}
				});
	}
}