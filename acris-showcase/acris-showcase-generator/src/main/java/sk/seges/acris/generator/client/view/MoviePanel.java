package sk.seges.acris.generator.client.view;

import sk.seges.acris.generator.client.event.RateMovieEvent;
import sk.seges.acris.generator.client.event.RateMovieEvent.RateMovieHandler;
import sk.seges.acris.generator.client.renderer.MovieRatingRenderer;
import sk.seges.acris.generator.shared.domain.api.MovieData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;

public class MoviePanel extends Composite {

	interface MoviePanelUiBinder extends UiBinder<Widget, MoviePanel> {}

	private static MoviePanelUiBinder uiBinder = GWT.create(MoviePanelUiBinder.class);

	@UiField
	HTMLPanel heading;

	@UiField
	HTMLPanel text;

	@UiField
	Image picture;

	@UiField
	HorizontalPanel rating;

	private MovieRatingRenderer ratingRenderer = new MovieRatingRenderer();

	public MoviePanel(final MovieData movie) {
		initWidget(uiBinder.createAndBindUi(this));

		picture.setUrl(GWT.getModuleBaseURL() + movie.getPicturePath());
		heading.getElement().setInnerHTML(movie.getName());
		text.getElement().setInnerHTML(movie.getDescription());

		ratingRenderer.render(movie.getRating(), rating);
		ratingRenderer.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new RateMovieEvent(movie));
			}
		});
	}

	public HandlerRegistration addRateMovieHandler(RateMovieHandler handler) {
		return addHandler(handler, RateMovieEvent.getType());
	}
}