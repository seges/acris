package sk.seges.acris.generator.offline.action;

import java.util.List;

import sk.seges.acris.common.util.Tuple;
import sk.seges.acris.generator.shared.action.GetAvailableNiceurlsAction;
import sk.seges.acris.generator.shared.action.GetAvailableNiceurlsResult;
import sk.seges.acris.generator.shared.action.GetLastProcessingTokenAction;
import sk.seges.acris.generator.shared.action.GetLastProcessingTokenResult;
import sk.seges.acris.generator.shared.action.GetOfflineContentHtmlAction;
import sk.seges.acris.generator.shared.action.GetOfflineContentHtmlResult;
import sk.seges.acris.generator.shared.action.ReadHtmlBodyFromFileAction;
import sk.seges.acris.generator.shared.action.ReadHtmlBodyFromFileResult;
import sk.seges.acris.generator.shared.action.SaveContentAction;
import sk.seges.acris.generator.shared.action.SaveContentResult;
import sk.seges.acris.generator.shared.action.WriteTextToFileAction;
import sk.seges.acris.generator.shared.action.WriteTextToFileResult;
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
	public void saveContent(GeneratorToken token, String contentText, final AsyncCallback<Boolean> callback) {
		actionManager.execute(new SaveContentAction(token, contentText), new DefaultAsyncCallback<SaveContentResult>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(SaveContentResult result) {
				callback.onSuccess(result.getResult());
			}
		});
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
	public void getOfflineContentHtml(String entryPointFileName, String header, String contentWrapper, String content, GeneratorToken token,
			String currentServerURL, final AsyncCallback<String> callback) {
		actionManager.execute(new GetOfflineContentHtmlAction(entryPointFileName, header, contentWrapper, content, token, currentServerURL),
				new DefaultAsyncCallback<GetOfflineContentHtmlResult>() {

					@Override
					public void onFailure(Throwable caught) {
						callback.onFailure(caught);
					}

					@Override
					public void onSuccess(GetOfflineContentHtmlResult result) {
						callback.onSuccess(result.getResult());
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
	public void writeTextToFile(String content, GeneratorToken token, final AsyncCallback<Void> callback) {
		actionManager.execute(new WriteTextToFileAction(content, token), new DefaultAsyncCallback<WriteTextToFileResult>() {

			@Override
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}

			@Override
			public void onSuccess(WriteTextToFileResult result) {
				callback.onSuccess((Void) null);
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