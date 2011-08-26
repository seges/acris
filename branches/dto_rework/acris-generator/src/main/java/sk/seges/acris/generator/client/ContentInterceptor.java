package sk.seges.acris.generator.client;

import java.util.ArrayList;

import sk.seges.acris.callbacks.client.ICallbackTrackingListener;
import sk.seges.acris.callbacks.client.RPCRequest;
import sk.seges.acris.callbacks.client.RPCRequestTracker;
import sk.seges.acris.callbacks.client.RequestState;
import sk.seges.acris.generator.client.context.api.GeneratorClientEnvironment;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.generator.shared.service.IGeneratorServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class ContentInterceptor {

	private IGeneratorServiceAsync generatorService;
	
	private GeneratorClientEnvironment generatorEnvironment;
	
	public ContentInterceptor(IGeneratorServiceAsync generatorService, GeneratorClientEnvironment generatorEnvironment) {
		this.generatorService = generatorService;
		this.generatorEnvironment = generatorEnvironment;
	}
	
	public void loadTokensForProcessing(final AsyncCallback<Void> callback) {
		
		final GeneratorToken defaultToken = generatorEnvironment.getTokensCache().getDefaultToken();
		
		generatorService.getDefaultGeneratorToken(defaultToken.getLanguage(), defaultToken.getWebId(), new AsyncCallback<GeneratorToken>() {

			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			public void onSuccess(GeneratorToken result) {
				if (result != null) {

					if (result.getWebId() != null && result.getWebId().length() > 0) {
						generatorEnvironment.setTopLevelDomain(result.getWebId());
					}
					
					defaultToken.setNiceUrl(result.getNiceUrl());
					defaultToken.setDefaultToken(true);

					getAvailableTokens(callback);
				} else {
					callback.onSuccess(null);
				}
			}
		});
	}

	
	private void getAvailableTokens(final AsyncCallback<Void> callback) {
		
		GeneratorToken currentToken = generatorEnvironment.getTokensCache().getDefaultToken();
		
		generatorService.getAvailableNiceurls(currentToken.getLanguage(), currentToken.getWebId(), new AsyncCallback<ArrayList<String>>() {

			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			public void onSuccess(ArrayList<String> result) {
				generatorEnvironment.getTokensCache().addTokens(result);
				callback.onSuccess(null);
			}
		});
	}

	
	private HandlerRegistration handler;
	
 	public void loadContent(final AsyncCallback<Void> callback) {

 		final GeneratorToken token = generatorEnvironment.getTokensCache().getCurrentToken();
 		
 		final Timer timer = new Timer() {

			@Override
			public void run() {
				GWT.log("Loading not finished sucesfully for niceurl " + token.getNiceUrl());
				RPCRequestTracker.getTracker().removeAllCallbacks();
				if (handler != null) {
					handler.removeHandler();
					handler = null;
				}
				callback.onFailure(new RuntimeException("Loading not finished sucesfully for niceurl " + token.getNiceUrl()));
			}
			
		};
		timer.schedule(60000);

		RPCRequestTracker.getTracker().registerCallbackListener(new ICallbackTrackingListener() {

			@Override
			public void onProcessingFinished(RPCRequest request) {
				if (request.getCallbackResult().equals(RequestState.REQUEST_FAILURE)) {
					timer.cancel();
					RPCRequestTracker.getTracker().removeAllCallbacks();
					callback.onFailure(request.getCaught());
				} else {
					GWT.log("Request finished. Waiting for next " + RPCRequestTracker.getRunningRequestStarted() + " requests for niceurl " + token.getNiceUrl());
					if (request.getParentRequest() == null) {
						timer.cancel();
						RPCRequestTracker.getTracker().removeAllCallbacks();
						callback.onSuccess(null);
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
				if (runningRequestsCount == newRunningRequestsCount) {
					GWT.log("No new RPC request started for niceurl " + token.getNiceUrl());
					//No new async request was started
					timer.cancel();
					RPCRequestTracker.getTracker().removeAllCallbacks();
					callback.onSuccess(null);
				} else {
					GWT.log("Waiting for " + (newRunningRequestsCount - runningRequestsCount) + " requests for niceurl " + token.getNiceUrl());
				}
			}
		};

		handler = History.addValueChangeHandler(valueChangeHandler);
		
	    if (token.getNiceUrl().equals(History.getToken())) {
	    	GWT.log("Loading already loaded niceurl " + token.getNiceUrl());
			valueChangeHandler.onValueChange(null);
	    } else {
	    	History.newItem(token.getNiceUrl());
	    }
	}

	public com.google.gwt.user.client.Element getRootElement() {
		return RootPanel.get().getElement();
	}
}