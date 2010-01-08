package sk.seges.acris.generator.client;

import java.util.List;

import sk.seges.acris.client.callback.ICallbackTrackingListener;
import sk.seges.acris.client.callback.RPCRequest;
import sk.seges.acris.client.callback.RPCRequestTracker;
import sk.seges.acris.client.callback.RequestState;
import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.generator.rpc.service.IGeneratorService;
import sk.seges.acris.generator.rpc.service.IGeneratorServiceAsync;
import sk.seges.acris.generator.rpc.service.IGeneratorServicesDefinition;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;

public abstract class GwtTestGenerateOfflineContent extends GWTTestCase implements IGeneratorServicesDefinition {
    
	private OfflineContentProvider offlineContentProvider;
	private ContentNavigator contentNavigator;
	private EntryPoint site;
	private ValueWrapper count = new ValueWrapper();

	private String currentWebId;
	
	private IGeneratorServiceAsync generatorService;
	
	public GwtTestGenerateOfflineContent() {
		super();
	}
	
	public String getModuleName() {
		return "sk.seges.synapso.generator.Generator";
	}

	public String getName() {
		return "testLoadContent";
	}

	protected abstract EntryPoint getEntryPoint(String webId);

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

	    delayTestFinish(9940000);

	    initializeService();
	    
	    count.value = 0;
	    offlineContentProvider = new OfflineContentProvider(getModuleName(), generatorService);

	    final GeneratorProperties generatorProperties = new GeneratorProperties(generatorService);

	    //Load generator properties from server
	    generatorProperties.load(new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				fail("Unable to load properties for generator run", caught);
			}

			public void onSuccess(Void result) {
			    contentNavigator = new ContentNavigator(generatorProperties, generatorService);
			    loadTokensForProcessing();
			}
		});
	}

	private void loadTokensForProcessing() {

		//Load last token for processing
	    contentNavigator.loadTokensForProcessing(new AsyncCallback<List<GeneratorToken>>() {

			public void onFailure(Throwable caught) {
				fail("Unable to obtain current content. Please check the log and connectivity on the RPC server side", caught);
			}

			public void onSuccess(List<GeneratorToken> result) {
				if (contentNavigator.hasNext()) {
					loadEntryPointHTML();
				} else {
					fail("No tokens available for processing. Finishing", null);
				}
			}
		});

	}
	
	private void loadEntryPointHTML() {
	    
		//Load entry point
		offlineContentProvider.getEntryPointHTML(new AsyncCallback<String>() {

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
		if (!contentNavigator.hasNext()) {
			//we are done
			return null;
		}

		count.value++;

		final GeneratorToken generatorToken = contentNavigator.next();
		
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
			site = getEntryPoint(generatorToken.getWebId());
			site.onModuleLoad();
		}  else {
			RPCRequestTracker.getTracker().removeAllCallbacks();
			loadContentForToken(generatorToken);
		}

		return generatorToken;
	}
	
	private void loadContentForToken(GeneratorToken generatorToken) {
		contentNavigator.loadContent(generatorToken, new AsyncCallback<GeneratorToken>() {

			@Override
			public void onFailure(Throwable caught) {
				fail("Unable to load content for token.", caught);
			}

			@Override
			public void onSuccess(GeneratorToken result) {
				saveContent(result);
				loadNextContent();
			}
		});
	}
	
	private GeneratorToken saveContent(GeneratorToken generatorToken) {
		
		String content = contentNavigator.getContent(generatorToken);
		
		offlineContentProvider.saveContent(content, generatorToken.getToken(), generatorToken.getLanguage(), generatorToken.getWebId(), new AsyncCallback<Void>() {
			public void onFailure(Throwable caught) {
				fail("Unable to write text to the file. " + caught);
				finalizeTest();
			}
	
			public void onSuccess(Void result) {
				count.value--;
				if (count.value == 0) {
					finalizeTest();
				}
			}
		});

		return generatorToken;
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