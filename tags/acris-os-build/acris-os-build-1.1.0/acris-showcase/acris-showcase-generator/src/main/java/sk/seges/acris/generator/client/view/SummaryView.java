package sk.seges.acris.generator.client.view;

import java.util.Collection;

import sk.seges.acris.generator.client.event.RateMovieEvent;
import sk.seges.acris.generator.client.event.RateMovieEvent.RateMovieHandler;
import sk.seges.acris.generator.client.event.SelectPromtEvent;
import sk.seges.acris.generator.client.event.SelectPromtEvent.SelectPromtHandler;
import sk.seges.acris.generator.client.presenter.SummaryPresenter.DialogOptions;
import sk.seges.acris.generator.client.presenter.SummaryPresenter.SummaryDisplay;
import sk.seges.acris.generator.shared.domain.api.MovieData;
import sk.seges.acris.widget.client.Dialog;
import sk.seges.acris.widget.client.WidgetFactory;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.ViewImpl;

public class SummaryView extends ViewImpl implements SummaryDisplay {

	private VerticalPanel container;

	@Inject
	public SummaryView() {
		container = new VerticalPanel();
	}

	@Override
	public Widget asWidget() {
		return container;
	}

	@Override
	public void setMovies(Collection<MovieData> movies) {

		container.clear();

		for (MovieData movie : movies) {
			MoviePanel moviePanel = new MoviePanel(movie);
			moviePanel.addRateMovieHandler(new RateMovieHandler() {
				
				@Override
				public void onRateMovie(RateMovieEvent event) {
					fireEvent(event);
				}
			});
			container.add(moviePanel);
		}
	}

	private HandlerManager handlerManager;

	public HandlerRegistration addClickHandler(ClickHandler handler) {
		return addHandler(handler, ClickEvent.getType());
	}

	protected final <H extends EventHandler> HandlerRegistration addHandler(final H handler, GwtEvent.Type<H> type) {
		return ensureHandlers().addHandler(type, handler);
	}

	HandlerManager ensureHandlers() {
		return handlerManager == null ? handlerManager = new HandlerManager(this) : handlerManager;
	}

	public void fireEvent(GwtEvent<?> event) {
		if (handlerManager != null) {
			handlerManager.fireEvent(event);
		}
	}

	@Override
	public HandlerRegistration registerHandlerForEvent(RateMovieHandler handler) {
		return addHandler(handler, RateMovieEvent.getType());
	}

	@Override
	public void showMessage(String message) {
		Dialog dialog = WidgetFactory.dialog();
		dialog.setContent(new HTML(message));
		dialog.addOptions(new Widget[] {WidgetFactory.button("OK")});
		dialog.center();
	}

	@Override
	public HandlerRegistration showPrompt(String message, SelectPromtHandler handler, DialogOptions... prompts) {
		Dialog dialog = WidgetFactory.dialog();
		dialog.setContent(new HTML(message));
		Widget[] widgets = new Widget[prompts.length];
		int i = 0;
		for (final DialogOptions dialogOption: prompts) {
			//TODO mem leak, deregistration is missing
			widgets[i] = WidgetFactory.button(dialogOption.getLabel(), new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					fireEvent(new SelectPromtEvent(dialogOption));
				}
			});
			i++;
		}
		
		HandlerRegistration registration = addHandler(handler, SelectPromtEvent.getType());
		
		dialog.addOptions(widgets);
		dialog.center();
		
		return registration;
	}
}