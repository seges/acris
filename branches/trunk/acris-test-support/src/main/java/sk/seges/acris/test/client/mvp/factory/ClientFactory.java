package sk.seges.acris.test.client.mvp.factory;

import sk.seges.acris.test.client.cardpay.display.CardPayDisplay;
import sk.seges.acris.test.shared.service.CardPayRemoteServiceAsync;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.place.shared.PlaceController;

public interface ClientFactory {

	EventBus getEventBus();

	PlaceController getPlaceController();

	CardPayDisplay getCardPayView();
	
	CardPayRemoteServiceAsync getCardPayService();
}