package sk.seges.acris.generator.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sk.seges.acris.callbacks.client.ICallbackTrackingListener;
import sk.seges.acris.callbacks.client.RPCRequest;
import sk.seges.acris.callbacks.client.RPCRequestTracker;
import sk.seges.acris.callbacks.client.RequestState;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.generator.shared.service.IGeneratorServiceAsync;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class ContentInterceptor implements Iterator<GeneratorToken>{

	private IGeneratorServiceAsync generatorService;
	
	private List<GeneratorToken> contents = new ArrayList<GeneratorToken>();
	private Iterator<GeneratorToken> contentsIterator;
	private GeneratorToken defaultToken = null;
	
	public ContentInterceptor(IGeneratorServiceAsync generatorService) {
		this.generatorService = generatorService;
	}
	
	public void loadTokensForProcessing(final AsyncCallback<List<GeneratorToken>> callback) {
		
		contents.clear();
		
		generatorService.getLastProcessingToken(new AsyncCallback<GeneratorToken>() {

			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			public void onSuccess(GeneratorToken result) {
				if (result != null) {
					defaultToken = new GeneratorToken();
					defaultToken.setWebId(result.getWebId());
					defaultToken.setNiceUrl(result.getNiceUrl());
					defaultToken.setDefaultToken(true);
					defaultToken.setLanguage(result.getLanguage());
					
					getAvailableTokens(result, new AsyncCallback<Void>() {

						public void onFailure(Throwable caught) {
							callback.onFailure(caught);
						}

						public void onSuccess(Void v) {
							callback.onSuccess(contents);
						}
					});
				} else {
					callback.onSuccess(contents);
				}
			}
		});
	}

	
	private GeneratorToken createGeneratorToken(final String niceurl, final String language, final String webId) {
		GeneratorToken content = new GeneratorToken();
		content.setNiceUrl(niceurl);
		content.setLanguage(language);
		content.setWebId(webId);
		return content;
	}

	private void getAvailableTokens(final GeneratorToken generatorToken, final AsyncCallback<Void> callback) {
		
		generatorService.getAvailableNiceurls(generatorToken.getLanguage(), generatorToken.getWebId(), new AsyncCallback<List<String>>() {

			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			public void onSuccess(List<String> result) {
				for (String niceurl : result) {
					contents.add(createGeneratorToken(niceurl, generatorToken.getLanguage(), generatorToken.getWebId()));
				}
				callback.onSuccess(null);
			}
		});
	}

	public boolean hasNext() {
		if (contentsIterator == null) {
			contentsIterator = contents.iterator();
		}
		return contentsIterator.hasNext();
	}

	public GeneratorToken next() {
		if (contentsIterator == null) {
			contentsIterator = contents.iterator();
		}

		GeneratorToken token = contentsIterator.next();
		
		if (token.getNiceUrl().equals(defaultToken.getNiceUrl()) &&  token.getWebId().equals(defaultToken.getWebId()) && token.getLanguage().equals(defaultToken.getLanguage())) {
			token.setDefaultToken(true);
		}
		
		return token;
	}

	public void remove() {
		if (contentsIterator == null) {
			contentsIterator = contents.iterator();
		}
		contentsIterator.remove();
	}
	
	private HandlerRegistration handler;
	
 	public void loadContent(final GeneratorToken token, final AsyncCallback<GeneratorToken> callback) {
 		Log.info("Loading content for niceurl " + token.getNiceUrl());

 		final Timer timer = new Timer() {

			@Override
			public void run() {
				Log.error("Loading not finished sucesfully for niceurl " + token.getNiceUrl());
				RPCRequestTracker.getTracker().removeAllCallbacks();
				if (handler != null) {
					handler.removeHandler();
					handler = null;
				}
				callback.onFailure(new RuntimeException("Loading not finished sucesfully for niceurl " + token.getNiceUrl()));
			}
			
		};
		timer.schedule(20000);

		final IntValueHolder requestsCounter = new IntValueHolder();
		
		RPCRequestTracker.getTracker().registerCallbackListener(new ICallbackTrackingListener() {

			@Override
			public void onProcessingFinished(RPCRequest request) {
				requestsCounter.value--;
				if (request.getCallbackResult().equals(RequestState.REQUEST_FAILURE)) {
					timer.cancel();
					callback.onFailure(request.getCaught());
				} else {
					requestsCounter.value--;
					Log.debug("Request finished. Waiting for next " + requestsCounter.value + " requests for niceurl " + token.getNiceUrl());
					if (request.getParentRequest() == null) {
						timer.cancel();
						RPCRequestTracker.getTracker().removeAllCallbacks();
						callback.onSuccess(token);
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

		final int runningRequestsCount = RPCRequestTracker.getRunningRequestStarted();
		
		ValueChangeHandler<String> valueChangeHandler = new ValueChangeHandler<String>() {
			
			@Override
			public void onValueChange(ValueChangeEvent<String> event) {
				if (handler != null) {
					handler.removeHandler();
					handler = null;
				}
				int newRunningRequestsCount = RPCRequestTracker.getRunningRequestStarted();
				requestsCounter.value = newRunningRequestsCount - runningRequestsCount;
				if (runningRequestsCount == newRunningRequestsCount) {
					Log.info("No new RPC request started for niceurl " + token.getNiceUrl());
					//No new async request was started
					timer.cancel();
					RPCRequestTracker.getTracker().removeAllCallbacks();
					callback.onSuccess(token);
				} else {
					Log.debug("Waiting for " + (newRunningRequestsCount - runningRequestsCount) + " requests for niceurl " + token.getNiceUrl());
				}
			}
		};

		handler = History.addValueChangeHandler(valueChangeHandler);
		
	    if (token.getNiceUrl().equals(History.getToken())) {
			Log.error("Loading already loaded niceurl " + token.getNiceUrl());
			valueChangeHandler.onValueChange(null);
	    } else {
	    	History.newItem(token.getNiceUrl());
	    }
	}

	public String getContent() {
		return RootPanel.get().toString();	
	}
}