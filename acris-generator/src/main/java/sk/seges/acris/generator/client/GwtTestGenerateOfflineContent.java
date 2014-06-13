package sk.seges.acris.generator.client;

import java.util.HashSet;
import java.util.Set;

import sk.seges.acris.callbacks.client.ICallbackTrackingListener;
import sk.seges.acris.callbacks.client.RPCRequest;
import sk.seges.acris.callbacks.client.RPCRequestTracker;
import sk.seges.acris.callbacks.client.RequestState;
import sk.seges.acris.callbacks.client.TrackingAsyncCallback;
import sk.seges.acris.generator.client.configuration.GeneratorConfiguration;
import sk.seges.acris.generator.client.context.DefaultGeneratorClientEnvironment;
import sk.seges.acris.generator.client.context.MapTokenCache;
import sk.seges.acris.generator.client.context.api.GeneratorClientEnvironment;
import sk.seges.acris.generator.client.factory.AnchorNodeCollectorFactory;
import sk.seges.acris.generator.client.factory.api.NodeCollectorFactory;
import sk.seges.acris.generator.client.json.params.OfflineClientWebParams;
import sk.seges.acris.generator.client.json.params.OfflineWebParamsJSO;
import sk.seges.acris.generator.client.performance.OperationTimer;
import sk.seges.acris.generator.client.performance.OperationTimer.Operation;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.generator.shared.service.IGeneratorServiceAsync;
import sk.seges.acris.shared.model.dto.WebSettingsDTO;
import sk.seges.acris.site.client.json.JSONModel;
import sk.seges.acris.site.shared.service.IWebSettingsRemoteServiceAsync;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ScriptElement;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public abstract class GwtTestGenerateOfflineContent extends GWTTestCase {

	private HtmlFilesHandler offlineContentProvider;
	private ContentInterceptor contentProvider;

	protected GeneratorClientEnvironment generatorEnvironment;
	
	private EntryPoint site;

	private OperationTimer timer;
	protected IGeneratorServiceAsync generatorService;
	protected IWebSettingsRemoteServiceAsync webSettingsService;
	protected OfflineClientWebParams offlineWebParams;
	
	private static final boolean PERFORMANCE_MONITOR = true;
	
	/**
	 * Default timeout for whole run of offline content generator Suppose to be never expired because generator should
	 * finish in correct way - by calling finalizeTest method.
	 */
	private static final int GENERATOR_TIMEOUT = 9940000;

	public GwtTestGenerateOfflineContent() {
		super();
	}

	public String getPageName() {
		return null;
	}
	
	public String getModuleName() {
		return "sk.seges.acris.generator.Generator";
	}

	public String getName() {
		return "testLoadContent";
	}

	protected abstract EntryPoint getEntryPoint(String webId, String lang, String webAlias);

	protected abstract IGeneratorServiceAsync getGeneratorService();
	protected abstract IWebSettingsRemoteServiceAsync getWebSettingsService();

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
		
		GeneratorConfiguration generatorConfiguration = GWT.create(GeneratorConfiguration.class);
		
		if (PERFORMANCE_MONITOR) {
			timer = new OperationTimer();
		}

		Log.debug("Initialize generator environment");
		generatorEnvironment = new DefaultGeneratorClientEnvironment(new MapTokenCache());
		GeneratorToken defaultToken = new GeneratorToken();
		defaultToken.setWebId(generatorConfiguration.getWebId());
		defaultToken.setLanguage(generatorConfiguration.getLanguage());
		defaultToken.setAlias(generatorConfiguration.getAlias());

		Log.debug("Default token is: web=" + generatorConfiguration.getWebId() + ", language=" + generatorConfiguration.getLanguage() + ", alias=" + generatorConfiguration.getAlias());

		generatorEnvironment.getTokensCache().setDefaultToken(defaultToken);

		prepareEnvironment(generatorConfiguration);

		generatorService = getGeneratorService();
		webSettingsService = getWebSettingsService();

		if (getPageName() == null) {
			offlineContentProvider = new HtmlFilesHandler(getModuleName(), generatorService);
		} else {
			offlineContentProvider = new HtmlFilesHandler(GWT.getModuleBaseURL(), getPageName(), generatorService);
		}
		
		contentProvider = getContentProvider(generatorEnvironment, generatorConfiguration);

		initializeWebSettings(generatorConfiguration.getWebId());
	}

	protected Set<NodeCollectorFactory> getNodeCollectorFactories() {
		Set<NodeCollectorFactory> collectors = new HashSet<NodeCollectorFactory>();
		
		if (offlineWebParams.supportsAutodetectMode()) {
			collectors.add(new AnchorNodeCollectorFactory());
		}
		
		return collectors;
	}
	
	protected void prepareEnvironment(GeneratorConfiguration generatorConfiguration) {
		if (generatorConfiguration.getProperties() != null && generatorConfiguration.getProperties().length() > 0) {
			ScriptElement scriptElement = Document.get().createScriptElement();
			scriptElement.setAttribute("type", "text/javascript");
			scriptElement.setAttribute("src", generatorConfiguration.getProperties());
			HtmlFilesHandler.getHeadElement().appendChild(scriptElement);
		}
	};

	protected ContentInterceptor getContentProvider(GeneratorClientEnvironment generatorEnvironment, GeneratorConfiguration generatorConfiguration) {
		return new ContentInterceptor(generatorService, generatorEnvironment, generatorConfiguration);
	}

	private void initializeWebSettings(String webId) {
		webSettingsService.getWebSettings(webId, new TrackingAsyncCallback<WebSettingsDTO>() {

			@Override
			public void onSuccessCallback(WebSettingsDTO webSettings) {
				String parameters = webSettings != null ? webSettings.getParameters() : null;
				if (parameters != null) {
					try {
						offlineWebParams = new OfflineWebParamsJSO(JSONModel.fromJson(parameters));
					} catch (Exception e) {
						Log.error("Error reading web params", e);
					}
				}
				
				loadTokensForProcessing();
			}

			@Override
			public void onFailureCallback(Throwable cause) {
				failure("Unable to obtain current content. Please check the log and connectivity on the RPC server side", cause);
				finalizeTest();
			}
		
		});
	}
	
	private void loadTokensForProcessing() {

		if (PERFORMANCE_MONITOR) {
			timer.start(Operation.GENERATOR_SERVER_READ_PROCESSING);
			timer.start(Operation.GENERATOR_CLIENT_PROCESSING);
		}
		
		//Load last token for processing
		Log.debug("Loading tokens for processing");

		contentProvider.loadTokensForProcessing(new AsyncCallback<Void>() {

			public void onFailure(Throwable caught) {
				if (PERFORMANCE_MONITOR) {
					timer.stop(Operation.GENERATOR_SERVER_READ_PROCESSING);
				}
				failure("Unable to obtain current content. Please check the log and connectivity on the RPC server side", caught);
				finalizeTest();
			}

			public void onSuccess(Void result) {
				if (PERFORMANCE_MONITOR) {
					timer.stop(Operation.GENERATOR_SERVER_READ_PROCESSING);
				}

				if (generatorEnvironment.getTokensCache().hasNext()) {

					Log.debug("Loading entry point");
					loadEntryPointHTML();
				} else {
					failure("No tokens available for processing. Finishing", null);
					finalizeTest();
				}
			}
		});

		if (PERFORMANCE_MONITOR) {
			timer.stop(Operation.GENERATOR_CLIENT_PROCESSING);
		}
	}

	private void loadEntryPointHTML() {

		if (PERFORMANCE_MONITOR) {
			timer.start(Operation.GENERATOR_CLIENT_PROCESSING);
		}

		Log.debug("Loading entry point HTML content");
		//Load entry point
		offlineContentProvider.getEntryPointBodyHtml(new AsyncCallback<String>() {

			public void onFailure(Throwable caught) {
				if (PERFORMANCE_MONITOR) {
					timer.stop(Operation.GENERATOR_SERVER_READ_PROCESSING);
				}
				failure("Unable to load entry point", caught);
				finalizeTest();
			}

			public void onSuccess(String result) {
				if (PERFORMANCE_MONITOR) {
					timer.stop(Operation.GENERATOR_SERVER_READ_PROCESSING);
					timer.start(Operation.GENERATOR_CLIENT_PROCESSING);
				}
				Log.debug("Cleaning UI and setting entry HTML");
				UIHelper.cleanUI();
				RootPanel.get().getElement().setInnerHTML(result);
				if (PERFORMANCE_MONITOR) {
					timer.stop(Operation.GENERATOR_CLIENT_PROCESSING);
				}
				loadNextContent();
			}
		});
		
		if (PERFORMANCE_MONITOR) {
			timer.stop(Operation.GENERATOR_CLIENT_PROCESSING);
			timer.start(Operation.GENERATOR_SERVER_READ_PROCESSING);
		}
	}

	private void logAwaitingRequests() {
		for (RPCRequest request: RPCRequestTracker.getAwaitingRequests()) {
			Log.info("Waiting for request: " + request.getName() + " to be finished");
		}
	}
	
	private GeneratorToken loadNextContent() {

		Log.debug("Loading next content");

		if (!generatorEnvironment.getTokensCache().hasNext()) {
			finalizeTest();
			return null;
		}

		if (PERFORMANCE_MONITOR) {
			timer.start(Operation.CONTENT_GENERATING);
			timer.start(Operation.GENERATOR_CLIENT_PROCESSING);
		}
		
		GeneratorToken generatorToken = generatorEnvironment.getTokensCache().next();
		
		Log.info("Generating offline content for niceurl [" + (generatorEnvironment.getTokensCache().getTokensCount() - 
				generatorEnvironment.getTokensCache().getWaitingTokensCount()) + " / " + generatorEnvironment.getTokensCache().getTokensCount() + "]: " + 
				generatorToken.getNiceUrl());

		RPCRequestTracker.getTracker().registerCallbackListener(new ICallbackTrackingListener() {

			@Override
			public void onProcessingFinished(RPCRequest request) {
				if (request.getCallbackResult().equals(RequestState.REQUEST_FAILURE)) {
					if (PERFORMANCE_MONITOR) {
						timer.stop(Operation.CONTENT_RENDERING);
						timer.stop(Operation.CONTENT_GENERATING);
					}
					failure("Unable to load content. See the previous errors in console.", null);
					RPCRequestTracker.getTracker().removeAllCallbacks();
					loadNextContent();
					//finalizeTest();
				} else {
					if (RPCRequestTracker.getRunningRequestStarted() == 0) {
						if (PERFORMANCE_MONITOR) {
							timer.stop(Operation.CONTENT_RENDERING);
						}
						
						RPCRequestTracker.getTracker().removeAllCallbacks();
						loadContentForToken();
					} else {
						logAwaitingRequests();
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

		if (PERFORMANCE_MONITOR) {
			timer.stop(Operation.GENERATOR_CLIENT_PROCESSING);
		}
		
		if (site == null) {
			site = getEntryPoint(generatorToken.getWebId(), generatorToken.getLanguage(), generatorToken.getAlias());
			if (PERFORMANCE_MONITOR) {
				timer.start(Operation.CONTENT_RENDERING);
			}
			site.onModuleLoad();
			
			generatorEnvironment.setServerURL(GWT.getHostPageBaseURL().replaceAll(GWT.getModuleName() + "/", ""));
		} else {
			RPCRequestTracker.getTracker().removeAllCallbacks();
			loadContentForToken();
		}

		return generatorToken;
	}

	private void loadContentForToken() {
		if (PERFORMANCE_MONITOR) {
			timer.start(Operation.CONTENT_RENDERING);
		}

		Log.debug("Loading content for current token");

		contentProvider.loadContent(new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				if (PERFORMANCE_MONITOR) {
					timer.stop(Operation.CONTENT_RENDERING);
					timer.stop(Operation.CONTENT_GENERATING);
				}
				
				failure("Unable to load content for nice-url " + generatorEnvironment.getTokensCache().getCurrentToken().getNiceUrl() + ".", caught);
				loadNextContent();
			}

			@Override
			public void onSuccess(Void result) {
				if (PERFORMANCE_MONITOR) {
					timer.stop(Operation.CONTENT_RENDERING);
				}
				Scheduler.get().scheduleDeferred(new ScheduledCommand() {

					@Override
					public void execute() {
						//Only for test, wait 2 seconds before save content
						new Timer() {
							@Override
							public void run() {
								saveAndLoadContent(generatorEnvironment);
							}
						}.schedule(1000);
					}
				});
			}
		});
		
		if (PERFORMANCE_MONITOR) {
			timer.stop(Operation.GENERATOR_CLIENT_PROCESSING);
			timer.start(Operation.GENERATOR_CLIENT_PROCESSING);
		}
	}

	  public final native String getValue(Element element) /*-{
	    return element.value;
	  }-*/;

	private void saveAndLoadContent(final GeneratorClientEnvironment generatorEnvironment) {

		if (PERFORMANCE_MONITOR) {
			timer.start(Operation.GENERATOR_DOM_MANIPULATION);
		}

		Log.debug("Preparing for saving the content");

		com.google.gwt.user.client.Element rootElement = contentProvider.getRootElement();

		Log.debug("Collecting anchors for further processing");

		for (NodeCollectorFactory nodeCollectorFactory: getNodeCollectorFactories()) {
			nodeCollectorFactory.create().collect(rootElement, generatorEnvironment);
		}

		Log.debug("Collected");

		//Currently used only for replacing &amp; with &
		//Tried many other generic solutions but didn't work 
		//	http://stackoverflow.com/questions/3700326/decode-amp-back-to-in-javascript
		//	http://www.webdeveloper.com/forum/archive/index.php/t-136026.html
		//Later will see whether other characters should be handled this way
		String content = DOM.getInnerHTML(rootElement);

		Log.trace("Content of the HTML: " + content);

		int contentLength = 0;
		
		do {
			contentLength = content.length();
			content = content.replace("&amp;", "&");
		} while (contentLength != content.length());

		Log.info("Content length: " + content.length());
		
		if (PERFORMANCE_MONITOR) {
			timer.stop(Operation.GENERATOR_DOM_MANIPULATION);
			timer.start(Operation.GENERATOR_CLIENT_PROCESSING);
		}

		Log.debug("Saving offline content for token " + generatorEnvironment.getTokensCache().getCurrentToken().getNiceUrl());

		offlineContentProvider.saveOfflineContent(content, generatorEnvironment.getTokensCache().getCurrentToken(), 
				generatorEnvironment.getServerURL(), new AsyncCallback<Void>() {

			@Override
			public void onFailure(Throwable caught) {
				if (PERFORMANCE_MONITOR) {
					timer.stop(Operation.CONTENT_GENERATING);
					Log.info(timer.report());
					timer.stop(Operation.GENERATOR_CLIENT_PROCESSING);
				}
				loadNextContent();
			}

			@Override
			public void onSuccess(Void result) {
				if (PERFORMANCE_MONITOR) {
					timer.stop(Operation.CONTENT_GENERATING);
					Log.info(timer.report());
					timer.stop(Operation.GENERATOR_CLIENT_PROCESSING);
				}
				loadNextContent();
			}
		});
	}

	private void failure(String msg, Throwable caught) {
		Log.error(msg + ", cause: " + caught.toString());
	}

	protected void finalizeEnvironment() {
		UIHelper.cleanUI();
	}

	private void finalizeTest() {
		if (PERFORMANCE_MONITOR) {
			Log.info(timer.report());
		}
		finalizeEnvironment();
		finishTest();
	}
}