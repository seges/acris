package sk.seges.acris.test.client.mvp.factory.core;

import sk.seges.acris.test.client.cardpay.display.CardPayDisplay;
import sk.seges.acris.test.client.cardpay.view.CardPayView;
import sk.seges.acris.test.client.mvp.factory.ClientFactory;
import sk.seges.acris.test.shared.service.CardPayRemoteService;
import sk.seges.acris.test.shared.service.CardPayRemoteServiceAsync;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.place.shared.PlaceController;

public class DefaultClientFactory implements ClientFactory {

	private static final EventBus eventBus = new SimpleEventBus();
	private static final PlaceController placeController = new PlaceController(eventBus);
	private static final CardPayDisplay cardPayView = new CardPayView();
	private static final CardPayRemoteServiceAsync cardPayService = GWT.create(CardPayRemoteService.class);

	@Override
	public EventBus getEventBus() {
		return eventBus;
	}

	@Override
	public PlaceController getPlaceController() {
		return placeController;
	}

	@Override
	public CardPayDisplay getCardPayView() {
		return cardPayView;
	}

	@Override
	public CardPayRemoteServiceAsync getCardPayService() {
		return cardPayService;
	}
}