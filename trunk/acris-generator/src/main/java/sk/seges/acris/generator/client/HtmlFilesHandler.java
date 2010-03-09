package sk.seges.acris.generator.client;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.generator.rpc.service.IGeneratorServiceAsync;
import sk.seges.acris.util.Pair;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.HeadElement;
import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * Loader for maintaining IO operations, e.g. loading entry point, saving
 * generated content
 * 
 * @author fat
 */
public class HtmlFilesHandler {

	private IGeneratorServiceAsync generatorService;

	private String initialContentFilename;
	
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
				new AsyncCallback<Pair<String, String>>() {

					public void onFailure(Throwable caught) {
						GWT.log(
								"Unable to read text from file. Please check entry html file: "
										+ initialContentFilename
										+ " and also RPC server side", caught);
						callback.onFailure(caught);
					}

					public void onSuccess(Pair<String, String> result) {
						if (result == null) {
							GWT.log(
									"Unable to load default content. Please check entry html file: "
											+ initialContentFilename, null);
							callback.onFailure(new RuntimeException(
									"Unable to load default content. Please check entry html file: "
											+ initialContentFilename));
						} else {
							Element el = getHeadElement();
							el.setInnerHTML(el.getInnerHTML() + "\n" + result.getFirst());
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
		
		generatorService.getOfflineContentHtml(initialContentFilename, header, content, token, currentServerURL, 
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