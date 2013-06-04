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
import com.google.gwt.user.client.rpc.ServiceDefTarget;

public class DefaultClientFactory implements ClientFactory {

	private static final EventBus eventBus = new SimpleEventBus();
	private static final PlaceController placeController = new PlaceController(eventBus);
	private static final CardPayDisplay cardPayView = new CardPayView();
	private static CardPayRemoteServiceAsync cardPayService = null;

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
		if (cardPayService == null ) {
			cardPayService = GWT.create(CardPayRemoteService.class);
			ServiceDefTarget endpoint = (ServiceDefTarget) cardPayService;
			endpoint.setServiceEntryPoint("test-service/cardpay");
		}
		return cardPayService;
	}
}