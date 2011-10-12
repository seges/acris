package sk.seges.acris.test.client.mvp.activity;

import sk.seges.acris.test.client.cardpay.activity.CardPayActivity;
import sk.seges.acris.test.client.cardpay.place.CardPayPlace;
import sk.seges.acris.test.client.mvp.factory.ClientFactory;

import com.google.gwt.activity.shared.Activity;
import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.place.shared.Place;

public class DefaultActivityMapper implements ActivityMapper {

	private ClientFactory clientFactory;

	public DefaultActivityMapper(ClientFactory clientFactory) {
		super();
		this.clientFactory = clientFactory;
	}

	@Override
	public Activity getActivity(Place place) {
		if (place instanceof CardPayPlace) {
			return new CardPayActivity(clientFactory);
		}
		return null;
	}
}