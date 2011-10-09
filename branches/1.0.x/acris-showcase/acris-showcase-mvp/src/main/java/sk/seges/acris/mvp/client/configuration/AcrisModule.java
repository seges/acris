package sk.seges.acris.mvp.client.configuration;

import com.google.inject.Singleton;
import com.philbeaudoin.gwtp.mvp.client.DefaultEventBus;
import com.philbeaudoin.gwtp.mvp.client.DefaultProxyFailureHandler;
import com.philbeaudoin.gwtp.mvp.client.EventBus;
import com.philbeaudoin.gwtp.mvp.client.RootPresenter;
import com.philbeaudoin.gwtp.mvp.client.gin.AbstractPresenterModule;
import com.philbeaudoin.gwtp.mvp.client.proxy.ParameterTokenFormatter;
import com.philbeaudoin.gwtp.mvp.client.proxy.PlaceManager;
import com.philbeaudoin.gwtp.mvp.client.proxy.ProxyFailureHandler;
import com.philbeaudoin.gwtp.mvp.client.proxy.TokenFormatter;

public class AcrisModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		bind(EventBus.class).to(DefaultEventBus.class).in(Singleton.class);
		bind(PlaceManager.class).to(AcrisPlaceManager.class).in(Singleton.class);
		bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);
		bind(RootPresenter.class).asEagerSingleton();

		bind(ProxyFailureHandler.class).to(DefaultProxyFailureHandler.class).in(Singleton.class);

	}

}
