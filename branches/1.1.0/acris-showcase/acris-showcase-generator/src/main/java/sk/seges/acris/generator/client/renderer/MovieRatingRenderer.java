package sk.seges.acris.generator.client.renderer;

import sk.seges.acris.generator.shared.domain.api.common.IRatingValue;
import sk.seges.acris.generator.shared.domain.api.common.RatingData;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.InsertPanel;

public class MovieRatingRenderer implements HtmlRenderer<RatingData, InsertPanel> {

	private static final String SEPARATOR = "/";
	private static final String IMAGES_DIR = "images" + SEPARATOR;

	private static final String STAR_IMAGE = IMAGES_DIR + "bookmark.png";
	private static final String EMPTY_IMAGE = IMAGES_DIR + "bookmark_low.png";

	private Image[] images;

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

	@Override
	public void render(RatingData element, InsertPanel target) {

		images = new Image[element.getRatingValues().length];

		for (int i = 0; i < element.getRatingValues().length; i++) {
			images[i] = new Image();
			target.add(images[i]);
			applyHover(element, i);
		}

		applyRatings(element.getRating(), element.getRatingValues());
	}

	private void applyRatings(IRatingValue rating, IRatingValue[] ratings) {
		int i = 0;

		for (IRatingValue ratingValue : ratings) {

			images[i].setUrl(GWT.getModuleBaseURL() + STAR_IMAGE);
			i++;

			if (rating.equals(ratingValue)) {
				break;
			}
		}

		for (; i < ratings.length; i++) {
			images[i].setUrl(GWT.getModuleBaseURL() + EMPTY_IMAGE);
		}
	}

	private void applyHover(final RatingData element, final int index) {

		images[index].addMouseOverHandler(new MouseOverHandler() {

			@Override
			public void onMouseOver(MouseOverEvent event) {
				for (int i = 0; i <= index; i++) {
					images[i].setUrl(GWT.getModuleBaseURL() + STAR_IMAGE);
				}

				for (int i = index + 1; i < element.getRatingValues().length; i++) {
					images[i].setUrl(GWT.getModuleBaseURL() + EMPTY_IMAGE);
				}

			}
		});

		images[index].addMouseOutHandler(new MouseOutHandler() {

			@Override
			public void onMouseOut(MouseOutEvent event) {
				applyRatings(element.getRating(), element.getRatingValues());
			}
		});

		images[index].addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				element.setRating(element.getRatingValues()[index]);
				fireEvent(event);
			}
		});
	}

	public void fireEvent(GwtEvent<?> event) {
		if (handlerManager != null) {
			handlerManager.fireEvent(event);
		}
	}
}