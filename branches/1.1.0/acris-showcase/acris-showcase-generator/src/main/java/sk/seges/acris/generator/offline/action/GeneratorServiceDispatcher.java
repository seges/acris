package sk.seges.acris.generator.offline.action;

import java.util.List;

import sk.seges.acris.common.util.Tuple;
import sk.seges.acris.generator.shared.action.GetAvailableNiceurlsAction;
import sk.seges.acris.generator.shared.action.GetAvailableNiceurlsResult;
import sk.seges.acris.generator.shared.action.GetLastProcessingTokenAction;
import sk.seges.acris.generator.shared.action.GetLastProcessingTokenResult;
import sk.seges.acris.generator.shared.action.ReadHtmlBodyFromFileAction;
import sk.seges.acris.generator.shared.action.ReadHtmlBodyFromFileResult;
import sk.seges.acris.generator.shared.action.WriteOfflineContentHtmlAction;
import sk.seges.acris.generator.shared.action.WriteOfflineContentHtmlResult;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.generator.shared.service.IGeneratorServiceAsync;
import sk.seges.acris.showcase.client.action.ActionManager;
import sk.seges.acris.showcase.client.action.DefaultAsyncCallback;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;

public class GeneratorServiceDispatcher implements IGeneratorServiceAsync {

	private ActionManager actionManager;

	@Inject	
	public GeneratorServiceDispatcher(ActionManager actionManager) {
		this.actionManager = actionManager;
	}

	@Override
	public void getLastProcessingToken(final AsyncCallback<GeneratorToken> callback) {
		actionManager.execute(new GetLastProcessingTokenAction(), new DefaultAsyncCallback<GetLastProcessingTokenResult>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(GetLastProcessingTokenResult result) {
				callback.onSuccess(result.getToken());
			}
		});
	}

	@Override
	public void writeOfflineContentHtml(String entryPointFileName, String header, String contentWrapper, String content,
			GeneratorToken token, String currentServerURL, final AsyncCallback<Void> callback) {
		actionManager.execute(new WriteOfflineContentHtmlAction(entryPointFileName, header, contentWrapper, content, token, currentServerURL),
				new DefaultAsyncCallback<WriteOfflineContentHtmlResult>() {

					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}

					@Override
					public void onSuccess(WriteOfflineContentHtmlResult result) {
						callback.onSuccess(null);
					}
				});
	}

	@Override
	public void readHtmlBodyFromFile(String filename, final AsyncCallback<Tuple<String, String>> callback) {
		actionManager.execute(new ReadHtmlBodyFromFileAction(filename), new DefaultAsyncCallback<ReadHtmlBodyFromFileResult>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(ReadHtmlBodyFromFileResult result) {
				callback.onSuccess(new Tuple<String, String>(result.getHeader(), result.getBody()));
			}
		});
	}

	@Override
	public void getAvailableNiceurls(String language, String webId, final AsyncCallback<List<String>> callback) {
		actionManager.execute(new GetAvailableNiceurlsAction(language, webId), new DefaultAsyncCallback<GetAvailableNiceurlsResult>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(GetAvailableNiceurlsResult result) {
				callback.onSuccess(result.getTokens());
			}
		});
	}
}