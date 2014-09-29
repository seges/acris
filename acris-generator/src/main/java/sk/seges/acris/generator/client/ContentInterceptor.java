package sk.seges.acris.generator.client;

import java.util.ArrayList;

import sk.seges.acris.callbacks.client.ICallbackTrackingListener;
import sk.seges.acris.callbacks.client.RPCRequest;
import sk.seges.acris.callbacks.client.RPCRequestTracker;
import sk.seges.acris.callbacks.client.RequestState;
import sk.seges.acris.generator.client.configuration.GeneratorConfiguration;
import sk.seges.acris.generator.client.context.api.GeneratorClientEnvironment;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.generator.shared.service.IGeneratorServiceAsync;
import sk.seges.sesam.shared.model.dto.ConjunctionDTO;
import sk.seges.sesam.shared.model.dto.FilterDTO;
import sk.seges.sesam.shared.model.dto.PageDTO;

import com.allen_sauer.gwt.log.client.Log;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class ContentInterceptor {

	private final IGeneratorServiceAsync generatorService;
	private final GeneratorClientEnvironment generatorEnvironment;
	private final GeneratorConfiguration generatorConfiguration;
	
	public ContentInterceptor(IGeneratorServiceAsync generatorService, GeneratorClientEnvironment generatorEnvironment, GeneratorConfiguration generatorConfiguration) {
		this.generatorService = generatorService;
		this.generatorEnvironment = generatorEnvironment;
		this.generatorConfiguration = generatorConfiguration;
	}
	
	public void loadTokensForProcessing(final AsyncCallback<Void> callback) {
		
		final GeneratorToken defaultToken = generatorEnvironment.getTokensCache().getDefaultToken();
		
		generatorService.getDefaultGeneratorToken(defaultToken.getLanguage(), defaultToken.getWebId(), new AsyncCallback<GeneratorToken>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(GeneratorToken result) {
				if (result != null) {

					if (result.getWebId() != null && result.getWebId().length() > 0) {
						generatorEnvironment.setTopLevelDomain(result.getWebId());
					}
					
					defaultToken.setNiceUrl(result.getNiceUrl());
					defaultToken.setDefaultToken(true);
					
					getAvailableTokens(callback, generatorConfiguration.getContentStartIndex(), generatorConfiguration.getContentPageSize());
				} else {
					callback.onSuccess(null);
				}
			}
		});
	}

	
	private void getAvailableTokens(final AsyncCallback<Void> callback, int startIndex, int pageSize) {
		
		GeneratorToken currentToken = generatorEnvironment.getTokensCache().getDefaultToken();

		PageDTO page = new PageDTO(startIndex, pageSize);
		ConjunctionDTO conjunction = FilterDTO.conjunction();
		conjunction.add(FilterDTO.eq("id.webId", currentToken.getWebId()));
		conjunction.add(FilterDTO.eq("id.language", currentToken.getLanguage()));
		page.setFilterable(conjunction);
		
		generatorService.getAvailableNiceurls(page, new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				generatorEnvironment.getTokensCache().addTokens(result);
				callback.onSuccess(null);
			}
		});
	}

	
	private HandlerRegistration handler;
	
	private void logAwaitingRequests() {
		for (RPCRequest request: RPCRequestTracker.getAwaitingRequests()) {
			Log.info("Waiting for request: " + request.getName() + " to be finished");
		}
	}
	
 	public void loadContent(final AsyncCallback<Void> callback) {

 		final GeneratorToken token = generatorEnvironment.getTokensCache().getCurrentToken();
 		
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
		timer.schedule(60000);

		RPCRequestTracker.getTracker().registerCallbackListener(new ICallbackTrackingListener() {

			@Override
			public void onProcessingFinished(RPCRequest request) {
				if (request.getCallbackResult().equals(RequestState.REQUEST_FAILURE)) {
					timer.cancel();
					RPCRequestTracker.getTracker().removeAllCallbacks();
					callback.onFailure(request.getCaught());
				} else {
					Log.debug("Request finished. Waiting for next " + RPCRequestTracker.getRunningRequestStarted() + " requests for niceurl " + token.getNiceUrl());
					if (RPCRequestTracker.getRunningRequestStarted() == 0) {
						timer.cancel();
						RPCRequestTracker.getTracker().removeAllCallbacks();
						callback.onSuccess(null);
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
					Log.info("No new RPC request started for niceurl " + token.getNiceUrl());
					//No new async request was started
					timer.cancel();
					RPCRequestTracker.getTracker().removeAllCallbacks();
					callback.onSuccess(null);
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

	@SuppressWarnings("deprecation")
	public com.google.gwt.user.client.Element getRootElement() {
		return RootPanel.get().getElement();
	}
}