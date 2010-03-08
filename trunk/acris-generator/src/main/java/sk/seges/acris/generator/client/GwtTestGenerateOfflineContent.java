package sk.seges.acris.generator.client;

import java.util.List;

import sk.seges.acris.callbacks.client.ICallbackTrackingListener;
import sk.seges.acris.callbacks.client.RPCRequest;
import sk.seges.acris.callbacks.client.RPCRequestTracker;
import sk.seges.acris.callbacks.client.RequestState;
import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.generator.rpc.service.IGeneratorService;
import sk.seges.acris.generator.rpc.service.IGeneratorServiceAsync;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;

public abstract class GwtTestGenerateOfflineContent extends GWTTestCase {
    
	private HtmlFilesHandler offlineContentProvider;
	private ContentInterceptor contentProvider;
	private EntryPoint site;
	private ValueWrapper count = new ValueWrapper();
	protected IGeneratorServiceAsync generatorService;
	
	/**
	 * Default timeout for whole run of offline content generator
	 * Suppose to be never expired because generator should finish
	 * in correct way - by calling finalizeTest method.
	 */
	private static final int GENERATOR_TIMEOUT = 9940000;
	
	public GwtTestGenerateOfflineContent() {
		super();
	}
	
	public String getModuleName() {
		return "sk.seges.acris.generator.Generator";
	}

	public String getName() {
		return "testLoadContent";
	}

	protected abstract EntryPoint getEntryPoint(String webId, String lang);
	
	protected abstract String getGeneratorServiceURL();
	
	private void initializeService() {
		generatorService = (IGeneratorServiceAsync) GWT.create(IGeneratorService.class);
		ServiceDefTarget generatorEndpoint = (ServiceDefTarget) generatorService;
		generatorEndpoint.setServiceEntryPoint(getGeneratorServiceURL());
	}
	
	/**
	 * Parse a URL and return a map of query parameters. If a parameter is supplied without =value, it will be defined
	 * as null.
	 * 
	 * @param url
	 *            the full or partial (ie, only location.search) URL to parse
	 * @return the map of parameter names to values
	 */
	public void testLoadContent() {

	    delayTestFinish(GENERATOR_TIMEOUT);

	    initializeService();
	    
	    count.value = 0;
	    offlineContentProvider = new HtmlFilesHandler(getModuleName(), generatorService);

	    contentProvider = getContentProvider();

	    loadTokensForProcessing();
	}

	protected ContentInterceptor getContentProvider() {
	    return new ContentInterceptor(generatorService);
	}

	private void loadTokensForProcessing() {

		//Load last token for processing
	    contentProvider.loadTokensForProcessing(new AsyncCallback<List<GeneratorToken>>() {

			public void onFailure(Throwable caught) {
				fail("Unable to obtain current content. Please check the log and connectivity on the RPC server side", caught);
			}

			public void onSuccess(List<GeneratorToken> result) {
				if (contentProvider.hasNext()) {
					count.value = result.size();
					loadEntryPointHTML();
				} else {
					fail("No tokens available for processing. Finishing", null);
				}
			}
		});

	}
	
	private void loadEntryPointHTML() {

		//Load entry point
		offlineContentProvider.getEntryPointBodyHtml(new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				fail("Unable to load entry point", caught);
			}

			public void onSuccess(String result) {
				UIHelper.cleanUI();
				RootPanel.get().getElement().setInnerHTML(result);
				loadNextContent();
			}
		});
	}
    
	private GeneratorToken loadNextContent() {
		if (!contentProvider.hasNext()) {
			//we are done
			return null;
		}

		final GeneratorToken generatorToken = contentProvider.next();
		
		RPCRequestTracker.getTracker().registerCallbackListener(new ICallbackTrackingListener() {

			@Override
			public void onProcessingFinished(RPCRequest request) {
				if (request.getCallbackResult().equals(RequestState.REQUEST_FAILURE)) {
					fail("Unable to load site. See the previous errors in console.", null);
				} else {
					if (request.getParentRequest() == null) {
						RPCRequestTracker.getTracker().removeAllCallbacks();
						loadContentForToken(generatorToken);
					}
				}
			}

			@Override
			public void onRequestStarted(RPCRequest request) {
			}

			@Override
			public void onResponseReceived(RPCRequest request) {
			}
			
		});

		if (site == null) {
			site = getEntryPoint(generatorToken.getWebId(), generatorToken.getLanguage());
			site.onModuleLoad();
		}  else {
			RPCRequestTracker.getTracker().removeAllCallbacks();
			loadContentForToken(generatorToken);
		}

		return generatorToken;
	}
	
	private void loadContentForToken(GeneratorToken generatorToken) {
		contentProvider.loadContent(generatorToken, new AsyncCallback<GeneratorToken>() {

			@Override
			public void onFailure(Throwable caught) {
				fail("Unable to load content for token.", caught);
			}

			@Override
			public void onSuccess(GeneratorToken result) {
				saveAndLoadContent(result);
			}
		});
	}
	
	private GeneratorToken saveAndLoadContent(final GeneratorToken generatorToken) {
		
		String content = contentProvider.getContent();

		final String currentServerURL = GWT.getHostPageBaseURL().replaceAll(GWT.getModuleName() + "/", "");
		
		offlineContentProvider.getOfflineContent(content, generatorToken, currentServerURL, new AsyncCallback<String>() {
			public void onFailure(Throwable caught) {
				fail("Unable to get offline content for token " + generatorToken.getNiceUrl() + ". " + caught);
				loadNextContent();
			}
	
			public void onSuccess(String result) {
				count.value--;
				processGeneratedContent(result, generatorToken, count.value == 0);
			}
		});
		
		return generatorToken;
	}
	
	protected void processGeneratedContent(String offlineContent, GeneratorToken token, final boolean finishTest) {
		generatorService.writeTextToFile(offlineContent, token,
				new AsyncCallback<Void>() {

					public void onFailure(Throwable caught) {
						fail("Unable to write text to the file. ", caught);
						loadNextContent();
					}

					public void onSuccess(Void result) {
						loadNextContent();
						if (finishTest) {
							finalizeTest();
						}
					}
				});
	}
	
	private void fail(String msg, Throwable caught) {
		GWT.log(msg, caught);
		if (caught != null) {
			fail(msg + caught);
		} else {
			fail(msg);
		}
		finalizeTest();
	}
	
	private void finalizeTest() {
		UIHelper.cleanUI();
		finishTest();
	}
}