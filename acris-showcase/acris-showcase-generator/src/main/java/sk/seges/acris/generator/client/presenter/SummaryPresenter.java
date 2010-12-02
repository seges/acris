package sk.seges.acris.generator.client.presenter;

import java.util.Collection;

import sk.seges.acris.generator.client.configuration.NameTokens;
import sk.seges.acris.generator.client.event.RateMovieEvent;
import sk.seges.acris.generator.client.event.RateMovieEvent.RateMovieHandler;
import sk.seges.acris.generator.client.event.SelectPromtEvent;
import sk.seges.acris.generator.client.event.SelectPromtEvent.SelectPromtHandler;
import sk.seges.acris.generator.client.i18n.MoviesMessage;
import sk.seges.acris.generator.client.presenter.SummaryPresenter.SummaryDisplay;
import sk.seges.acris.generator.client.presenter.SummaryPresenter.SummaryProxy;
import sk.seges.acris.generator.shared.action.FetchMoviesAction;
import sk.seges.acris.generator.shared.action.FetchMoviesResult;
import sk.seges.acris.generator.shared.action.GenerateOfflineAction;
import sk.seges.acris.generator.shared.action.GenerateOfflineResult;
import sk.seges.acris.generator.shared.action.SaveRatingAction;
import sk.seges.acris.generator.shared.action.SaveRatingResult;
import sk.seges.acris.generator.shared.domain.api.MovieData;
import sk.seges.acris.showcase.client.action.ActionManager;
import sk.seges.acris.showcase.client.action.DefaultAsyncCallback;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.Presenter;
import com.gwtplatform.mvp.client.View;
import com.gwtplatform.mvp.client.annotations.NameToken;
import com.gwtplatform.mvp.client.annotations.ProxyStandard;
import com.gwtplatform.mvp.client.proxy.Place;
import com.gwtplatform.mvp.client.proxy.Proxy;
import com.gwtplatform.mvp.client.proxy.RevealRootContentEvent;

public class SummaryPresenter extends Presenter<SummaryDisplay, SummaryProxy> {

	private ActionManager actionManager;

	private MoviesMessage messages = GWT.create(MoviesMessage.class);

	@Inject
	public SummaryPresenter(ActionManager actionManager, EventBus eventBus, SummaryDisplay view, SummaryProxy proxy) {
		super(eventBus, view, proxy);
		this.actionManager = actionManager;
	}

	public static enum DialogOptions {

		YES("Yes"), NO("No");

		private String label;

		DialogOptions(String label) {
			this.label = label;
		}

		public String getLabel() {
			return label;
		}
	}

	@ProxyStandard
	@NameToken(NameTokens.HOME_PAGE)
	public interface SummaryProxy extends Proxy<SummaryPresenter>, Place {}

	public interface SummaryDisplay extends View {

		void setMovies(Collection<MovieData> movies);

		HandlerRegistration registerHandlerForEvent(RateMovieHandler handler);

		void showMessage(String message);

		HandlerRegistration showPrompt(String message, SelectPromtHandler handler, DialogOptions... prompts);
	}

	@Override
	protected void revealInParent() {
		RevealRootContentEvent.fire(this, this);
	}

	@Override
	protected void onReveal() {
		actionManager.execute(new FetchMoviesAction(), new DefaultAsyncCallback<FetchMoviesResult>() {

			@Override
			public void onSuccess(FetchMoviesResult result) {
				getView().setMovies(result.getMovies());
			}
		});

		registerHandler(getView().registerHandlerForEvent(new RateMovieHandler() {

			@Override
			public void onRateMovie(RateMovieEvent event) {
				actionManager.execute(new SaveRatingAction(event.getMovie()), new DefaultAsyncCallback<SaveRatingResult>() {

					@Override
					public void onSuccess(SaveRatingResult result) {
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
				});
			}
		}));
	}
}