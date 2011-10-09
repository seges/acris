package sk.seges.acris.mvp.client.configuration;

import sk.seges.acris.mvp.client.presenter.user.LoginPresenter.LoginProxy;

import com.google.inject.Inject;
import com.philbeaudoin.gwtp.mvp.client.EventBus;
import com.philbeaudoin.gwtp.mvp.client.proxy.PlaceManagerImpl;
import com.philbeaudoin.gwtp.mvp.client.proxy.Proxy;
import com.philbeaudoin.gwtp.mvp.client.proxy.TokenFormatter;

public class AcrisPlaceManager extends PlaceManagerImpl {

	private final Proxy<?> defaultProxy;

	@Inject
	public AcrisPlaceManager(EventBus eventBus, TokenFormatter tokenFormatter, LoginProxy defaultProxy) {
		super(eventBus, tokenFormatter);
		this.defaultProxy = defaultProxy;
	}

	@Override
	public void revealDefaultPlace() {
		defaultProxy.reveal();
	}
}