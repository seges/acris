package sk.seges.acris.generator.client;

import java.util.List;

import sk.seges.acris.callbacks.client.ICallbackTrackingListener;
import sk.seges.acris.callbacks.client.RPCRequest;
import sk.seges.acris.callbacks.client.RPCRequestTracker;
import sk.seges.acris.callbacks.client.RequestState;
import sk.seges.acris.generator.client.performance.OperationTimer;
import sk.seges.acris.generator.client.performance.OperationTimer.Operation;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.generator.shared.service.IGeneratorServiceAsync;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public abstract class GwtTestGenerateOfflineContent extends GWTTestCase {

	private HtmlFilesHandler offlineContentProvider;
	private ContentInterceptor contentProvider;

	private static EntryPoint site;
	private static String webId;

	private IntValueHolder count = new IntValueHolder();
	private int totalCount = 0;
	private OperationTimer timer;
	protected IGeneratorServiceAsync generatorService;

	/**
	 * Default timeout for whole run of offline content generator Suppose to be never expired because generator should
	 * finish in correct way - by calling finalizeTest method.
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

	protected abstract IGeneratorServiceAsync getGeneratorService();

	/**
	 * Parse a URL and return a map of query parameters. If a parameter is supplied without =value, it will be defined
	 * as null.
	 * 
	 * @param url the full or partial (ie, only location.search) URL to parse
	 * @return the map of parameter names to values
	 */
	public void testLoadContent() {

		delayTestFinish(GENERATOR_TIMEOUT);

		GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {

			@Override
			public void onUncaughtException(Throwable e) {
				Log.error("Uncaught exception", e);
			}
		});

		timer = new OperationTimer();

		prepareEnvironment();

		generatorService = getGeneratorService();

		count.value = 0;
		offlineContentProvider = new HtmlFilesHandler(getModuleName(), generatorService);

		contentProvider = getContentProvider();

		loadTokensForProcessing();
	}

	protected void prepareEnvironment() {
	};

	protected ContentInterceptor getContentProvider() {
		return new ContentInterceptor(generatorService);
	}

	private void loadTokensForProcessing() {

		timer.start(Operation.GENERATOR_SERVER_READ_PROCESSING);
		
		//Load last token for processing
		contentProvider.loadTokensForProcessing(new AsyncCallback<List<GeneratorToken>>() {

			public void onFailure(Throwable caught) {
				timer.stop(Operation.GENERATOR_SERVER_READ_PROCESSING);
				failure("Unable to obtain current content. Please check the log and connectivity on the RPC server side", caught);
				finalizeTest();
			}

			public void onSuccess(List<GeneratorToken> result) {
				timer.stop(Operation.GENERATOR_SERVER_READ_PROCESSING);
				if (contentProvider.hasNext()) {
					totalCount = result.size();
					count.value = result.size();
					loadEntryPointHTML();
				} else {
					failure("No tokens available for processing. Finishing", null);
					finalizeTest();
				}
			}
		});

	}

	private void loadEntryPointHTML() {

		timer.start(Operation.GENERATOR_SERVER_READ_PROCESSING);

		//Load entry point
		offlineContentProvider.getEntryPointBodyHtml(new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				timer.stop(Operation.GENERATOR_SERVER_READ_PROCESSING);
				failure("Unable to load entry point", caught);
				finalizeTest();
			}

			public void onSuccess(String result) {
				timer.stop(Operation.GENERATOR_SERVER_READ_PROCESSING);
				timer.start(Operation.GENERATOR_CLIENT_PROCESSING);
				UIHelper.cleanUI();
				RootPanel.get().getElement().setInnerHTML(result);
				timer.stop(Operation.GENERATOR_CLIENT_PROCESSING);
				loadNextContent();
			}
		});
	}

	private GeneratorToken loadNextContent() {
		if (!contentProvider.hasNext()) {
			//we are done
			return null;
		}

		timer.start(Operation.CONTENT_GENERATING);
		timer.start(Operation.GENERATOR_CLIENT_PROCESSING);

		final GeneratorToken generatorToken = contentProvider.next();

		RPCRequestTracker.getTracker().registerCallbackListener(new ICallbackTrackingListener() {

			@Override
			public void onProcessingFinished(RPCRequest request) {
				timer.stop(Operation.CONTENT_RENDERING);
				if (request.getCallbackResult().equals(RequestState.REQUEST_FAILURE)) {
					timer.stop(Operation.CONTENT_GENERATING);
					failure("Unable to load site. See the previous errors in console.", null);
					finalizeTest();
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

		timer.stop(Operation.GENERATOR_CLIENT_PROCESSING);
		timer.start(Operation.CONTENT_RENDERING);

		if (site == null || (webId != null && webId != generatorToken.getWebId())) {
			webId = generatorToken.getWebId();
			site = getEntryPoint(generatorToken.getWebId(), generatorToken.getLanguage());
			site.onModuleLoad();
		} else {
			RPCRequestTracker.getTracker().removeAllCallbacks();
			timer.stop(Operation.CONTENT_RENDERING);
			loadContentForToken(generatorToken);
		}

		return generatorToken;
	}

	private void loadContentForToken(final GeneratorToken generatorToken) {
		timer.start(Operation.CONTENT_RENDERING);
		contentProvider.loadContent(generatorToken, new AsyncCallback<GeneratorToken>() {

			@Override
			public void onFailure(Throwable caught) {
				timer.stop(Operation.CONTENT_RENDERING);
				timer.stop(Operation.CONTENT_GENERATING);
				failure("Unable to load content for nice-url " + generatorToken.getNiceUrl() + ".", caught);
				count.value--;
				loadNextContent();
			}

			@Override
			public void onSuccess(GeneratorToken result) {
				timer.stop(Operation.CONTENT_RENDERING);
				saveAndLoadContent(result);
			}
		});
	}

	private GeneratorToken saveAndLoadContent(final GeneratorToken generatorToken) {

		String content = contentProvider.getContent();

		Log.info("Generating offline content for niceurl [" + (totalCount - count.value + 1) + " / " + totalCount + "]: " + generatorToken.getNiceUrl());

		final String currentServerURL = GWT.getHostPageBaseURL().replaceAll(GWT.getModuleName() + "/", "");

		timer.start(Operation.GENERATOR_SERVER_WRITE_PROCESSING);
		
		offlineContentProvider.saveOfflineContent(content, generatorToken, currentServerURL, new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				timer.stop(Operation.CONTENT_GENERATING);
				timer.stop(Operation.GENERATOR_SERVER_WRITE_PROCESSING);
				failure("Unable to get offline content for token " + generatorToken.getNiceUrl() + ". ", caught);
				count.value--;
				loadNextContent();
			}

			public void onSuccess(Void result) {
				count.value--;
				timer.stop(Operation.CONTENT_GENERATING);
				timer.stop(Operation.GENERATOR_SERVER_WRITE_PROCESSING);
				if (count.value == 0) {
					finalizeTest();
				}
				loadNextContent();
			}
		});

		return generatorToken;
	}

	private void failure(String msg, Throwable caught) {
		Log.error(msg);
		//GWT.log(msg, caught);
		//finalizeTest();
	}

	protected void finalizeEnvironment() {
		UIHelper.cleanUI();
	}

	private void finalizeTest() {
		Log.info(timer.report());
		finalizeEnvironment();
		finishTest();
	}
}