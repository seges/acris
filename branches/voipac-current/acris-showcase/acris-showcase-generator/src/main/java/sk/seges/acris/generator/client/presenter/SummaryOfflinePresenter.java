package sk.seges.acris.generator.client.presenter;

import sk.seges.acris.generator.shared.action.GenerateOfflineAction;
import sk.seges.acris.generator.shared.action.GenerateOfflineResult;
import sk.seges.acris.showcase.client.action.ActionManager;
import sk.seges.acris.showcase.client.action.DefaultAsyncCallback;
import sk.seges.acris.showcase.mora.client.event.SelectPromtEvent;
import sk.seges.acris.showcase.mora.client.event.SelectPromtEvent.SelectPromtHandler;
import sk.seges.acris.showcase.mora.client.i18n.MoviesMessage;
import sk.seges.acris.showcase.mora.client.presenter.SummaryPresenter;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;

public class SummaryOfflinePresenter extends SummaryPresenter {

	private MoviesMessage messages = GWT.create(MoviesMessage.class);

	@Inject
	public SummaryOfflinePresenter(ActionManager actionManager, EventBus eventBus, SummaryDisplay view, SummaryProxy proxy) {
		super(actionManager, eventBus, view, proxy);
	}

	@Override
	protected void ratingSaved() {
		getView().showPrompt(messages.savedRatings(), new SelectPromtHandler() {

			@Override
			public void onSelectPromt(SelectPromtEvent event) {
				if (DialogOptions.YES == event.getDialogOptions()) {
					actionManager.execute(new GenerateOfflineAction(), new DefaultAsyncCallback<GenerateOfflineResult>() {

						@Override
						public void onSuccess(GenerateOfflineResult result) {
							if (result.isResult()) {
								getView().showMessage(messages.offlineGenerated());
							} else {
								getView().showMessage(messages.failure());
							}
						}
					});
				}
			}

		}, DialogOptions.YES, DialogOptions.NO);

	}
}