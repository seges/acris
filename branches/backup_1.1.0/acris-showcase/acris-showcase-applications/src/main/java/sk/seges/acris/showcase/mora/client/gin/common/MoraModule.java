package sk.seges.acris.showcase.mora.client.gin.common;

import sk.seges.acris.showcase.client.presenter.FailureHandler;
import sk.seges.acris.showcase.client.presenter.core.ErrorPresenter;
import sk.seges.acris.showcase.mora.client.gin.MoraPlaceManager;
import sk.seges.acris.showcase.mora.client.presenter.RootMoviePresenter;
import sk.seges.acris.showcase.mora.client.presenter.RootMoviePresenter.RootMovieView;
import sk.seges.acris.showcase.mora.client.presenter.SummaryPresenter;
import sk.seges.acris.showcase.mora.client.presenter.SummaryPresenter.SummaryDisplay;
import sk.seges.acris.showcase.mora.client.presenter.SummaryPresenter.SummaryProxy;
import sk.seges.acris.showcase.mora.client.view.SummaryView;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.inject.Singleton;
import com.gwtplatform.mvp.client.DefaultProxyFailureHandler;
import com.gwtplatform.mvp.client.RootPresenter;
import com.gwtplatform.mvp.client.RootPresenter.RootView;
import com.gwtplatform.mvp.client.gin.AbstractPresenterModule;
import com.gwtplatform.mvp.client.proxy.ParameterTokenFormatter;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;
import com.gwtplatform.mvp.client.proxy.TokenFormatter;

public class MoraModule extends AbstractPresenterModule {

	@Override
	protected void configure() {
		bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);
		bind(PlaceManager.class).to(MoraPlaceManager.class).in(Singleton.class);
		bind(TokenFormatter.class).to(ParameterTokenFormatter.class).in(Singleton.class);

		bind(RootPresenter.class).to(RootMoviePresenter.class).asEagerSingleton();
		bind(RootView.class).to(RootMovieView.class).asEagerSingleton();

		bind(FailureHandler.class).to(ErrorPresenter.class);
		
		bind(ProxyFailureHandler.class).to(DefaultProxyFailureHandler.class).in(Singleton.class);

		bindPresenter(SummaryPresenter.class, SummaryDisplay.class, SummaryView.class, SummaryProxy.class);
	}
}
