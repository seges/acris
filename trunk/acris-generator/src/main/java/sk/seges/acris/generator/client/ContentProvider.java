package sk.seges.acris.generator.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import sk.seges.acris.callbacks.client.ICallbackTrackingListener;
import sk.seges.acris.callbacks.client.RPCRequest;
import sk.seges.acris.callbacks.client.RPCRequestTracker;
import sk.seges.acris.callbacks.client.RequestState;
import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.generator.rpc.service.IGeneratorServiceAsync;

import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.RootPanel;

public class ContentProvider implements Iterator<GeneratorToken>{

	private IGeneratorServiceAsync generatorService;
	
	private List<GeneratorToken> contents = new ArrayList<GeneratorToken>();
	private Iterator<GeneratorToken> contentsIterator;

	public ContentProvider(IGeneratorServiceAsync generatorService) {
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
		return contentsIterator.next();
	}

	public void remove() {
		if (contentsIterator == null) {
			contentsIterator = contents.iterator();
		}
		contentsIterator.remove();
	}
	
 	public void loadContent(final GeneratorToken token, final AsyncCallback<GeneratorToken> callback) {
		RPCRequestTracker.getTracker().registerCallbackListener(new ICallbackTrackingListener() {

			@Override
			public void onProcessingFinished(RPCRequest request) {
				if (request.getCallbackResult().equals(RequestState.REQUEST_FAILURE)) {
					callback.onFailure(request.getCaught());
				} else {
					if (request.getParentRequest() == null) {
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

		int runningRequestsCount = RPCRequestTracker.getRunningRequestStarted();

		History.newItem(token.getNiceUrl());
		
		int newRunningRequestsCount = RPCRequestTracker.getRunningRequestStarted();
		if (runningRequestsCount == newRunningRequestsCount) {
			//No new async request was started

			RPCRequestTracker.getTracker().removeAllCallbacks();
			new Timer() {

				@Override
				public void run() {
					callback.onSuccess(token);
				}
				
			}.schedule(5000);
		}
	}

	public String getContent() {
		return RootPanel.get().toString();	
	}
}