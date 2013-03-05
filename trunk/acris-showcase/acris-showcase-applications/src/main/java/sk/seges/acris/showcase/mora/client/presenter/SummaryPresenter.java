package sk.seges.acris.showcase.mora.client.presenter;

import java.util.Collection;

import sk.seges.acris.showcase.client.action.ActionManager;
import sk.seges.acris.showcase.client.action.DefaultAsyncCallback;
import sk.seges.acris.showcase.mora.client.configuration.NameTokens;
import sk.seges.acris.showcase.mora.client.event.RateMovieEvent;
import sk.seges.acris.showcase.mora.client.event.RateMovieEvent.RateMovieHandler;
import sk.seges.acris.showcase.mora.client.event.SelectPromtEvent.SelectPromtHandler;
import sk.seges.acris.showcase.mora.client.presenter.SummaryPresenter.SummaryDisplay;
import sk.seges.acris.showcase.mora.client.presenter.SummaryPresenter.SummaryProxy;
import sk.seges.acris.showcase.mora.shared.action.FetchMoviesAction;
import sk.seges.acris.showcase.mora.shared.action.FetchMoviesResult;
import sk.seges.acris.showcase.mora.shared.action.SaveRatingAction;
import sk.seges.acris.showcase.mora.shared.action.SaveRatingResult;
import sk.seges.acris.showcase.mora.shared.domain.api.MovieData;

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

	protected ActionManager actionManager;

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
						ratingSaved();
					}
				});
			}
		}));
	}
	
	protected void ratingSaved() {
	}
}