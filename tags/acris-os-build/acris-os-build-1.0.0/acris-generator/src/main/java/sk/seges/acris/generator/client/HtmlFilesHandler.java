package sk.seges.acris.generator.client;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.generator.rpc.service.IGeneratorServiceAsync;
import sk.seges.acris.util.Tuple;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Loader used to maintain IO operations, e.g. entry point loading, saving
 * generated content
 * 
 * @author fat
 */
public class HtmlFilesHandler {

	private IGeneratorServiceAsync generatorService;

	private String initialContentFilename;
	private String bodyContentWrapper;
	
	public HtmlFilesHandler(String moduleName, IGeneratorServiceAsync generatorService) {
		
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

	public void getEntryPointBodyHtml(final AsyncCallback<String> callback) {
		
		generatorService.readHtmlBodyFromFile(initialContentFilename,
				new AsyncCallback<Tuple<String, String>>() {

					public void onFailure(Throwable caught) {
						GWT.log(
								"Unable to read text from file. Please check entry html file: "
										+ initialContentFilename
										+ " and also RPC server side", caught);
						callback.onFailure(caught);
					}

					public void onSuccess(Tuple<String, String> result) {
						if (result == null) {
							GWT.log(
									"Unable to load default content. Please check entry html file: "
											+ initialContentFilename, null);
							callback.onFailure(new RuntimeException(
									"Unable to load default content. Please check entry html file: "
											+ initialContentFilename));
						} else {
							UIHelper.cleanUI();
							
							bodyContentWrapper = result.getSecond();
							callback.onSuccess(result.getSecond());
						}
					}
				});
	}

	public static native HeadElement getHeadElement() /*-{
	    return $doc.getElementsByTagName("head")[0];
	}-*/;

	public void getOfflineContent(String content, GeneratorToken token, String currentServerURL, final AsyncCallback<String> callback) {
		
		String header = getHeadElement().getInnerHTML();

		header = header.replaceAll(currentServerURL + GWT.getModuleName() + "/", "");

		generatorService.getOfflineContentHtml(initialContentFilename, header, bodyContentWrapper, content, token, currentServerURL, 
				new AsyncCallback<String>() {

					public void onFailure(Throwable caught) {
						GWT.log("Unable to write text to the file. ", caught);
						callback.onFailure(caught);
					}

					public void onSuccess(String result) {
						callback.onSuccess(result);
					}
				});
	}
}