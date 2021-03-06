package sk.seges.acris.showcase.mora.client.gin;

import sk.seges.acris.showcase.mora.client.configuration.NameTokens;

import com.google.gwt.event.shared.EventBus;
import com.google.inject.Inject;
import com.gwtplatform.mvp.client.proxy.PlaceManagerImpl;
import com.gwtplatform.mvp.client.proxy.PlaceRequest;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

/**
 * Mora - MOvie RAtings
 */
public class MoraPlaceManager extends PlaceManagerImpl {

	@Inject
	public MoraPlaceManager(EventBus eventBus, TokenFormatter tokenFormatter) {
		super(eventBus, tokenFormatter);
	}

	@Override
	public void revealDefaultPlace() {
		revealPlace(new PlaceRequest(NameTokens.HOME_PAGE));
	}

}
