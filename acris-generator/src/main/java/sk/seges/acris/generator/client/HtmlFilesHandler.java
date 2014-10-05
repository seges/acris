package sk.seges.acris.generator.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.user.client.rpc.AsyncCallback;
import sk.seges.acris.common.util.Tuple;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.generator.shared.service.IGeneratorServiceAsync;

/**
 * Loader used to maintain IO operations, e.g. entry point loading, saving
 * generated content
 * 
 * @author Peter Simun
 */
public class HtmlFilesHandler {

	private IGeneratorServiceAsync generatorService;

	private String initialContentFilename;
	private String bodyContentWrapper;
	
	public HtmlFilesHandler(String moduleName, IGeneratorServiceAsync generatorService) {
		this(GWT.getModuleBaseURL(), getPageNameFromModule(moduleName), generatorService);
	}

	private static final String getPageNameFromModule(String moduleName) {
		int lastDotIndex = moduleName.lastIndexOf(".");
		return lastDotIndex == -1 ? null : moduleName.substring(lastDotIndex + 1);
	}
	
	public HtmlFilesHandler(String moduleName, String pageName, IGeneratorServiceAsync generatorService) {
		this.generatorService = generatorService;
		initialContentFilename = moduleName + pageName + ".html";
	}

	public void getEntryPointBodyHtml(final AsyncCallback<String> callback) {
		
		generatorService.readHtmlBodyFromFile(initialContentFilename,new AsyncCallback<Tuple<String, String>>() {

			public void onFailure(Throwable caught) {
				GWT.log("Unable to read text from file. Please check entry html file: "
								+ initialContentFilename + " and also RPC server side", caught);
				callback.onFailure(caught);
			}

			public void onSuccess(Tuple<String, String> result) {
				if (result == null) {
					GWT.log("Unable to load default content. Please check entry html file: " + initialContentFilename, null);
					callback.onFailure(new RuntimeException("Unable to load default content. Please check entry html file: " + initialContentFilename));
				} else {
					bodyContentWrapper = result.getSecond();
					callback.onSuccess(result.getSecond());
				}
			}
		});
	}

	public static native HeadElement getHeadElement() /*-{
	    return $doc.getElementsByTagName("head")[0];
	}-*/;

	public void saveOfflineContent(String content, GeneratorToken token, String currentServerURL, final AsyncCallback<Void> callback) {

		String header = getHeadElement().getInnerHTML();
		header = header.replaceAll(currentServerURL + GWT.getModuleName() + "/", "");

		generatorService.writeOfflineContentHtml(initialContentFilename, header, bodyContentWrapper, content, token, currentServerURL, callback);
	}
}