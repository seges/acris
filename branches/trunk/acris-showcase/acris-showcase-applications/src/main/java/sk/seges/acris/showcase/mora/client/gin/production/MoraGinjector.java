package sk.seges.acris.showcase.mora.client.gin.production;

import sk.seges.acris.showcase.client.presenter.core.ErrorPresenter;
import sk.seges.acris.showcase.mora.client.gin.common.MoraModule;
import sk.seges.acris.showcase.mora.client.gin.smartgwt.SmartGWTViews;
import sk.seges.acris.showcase.mora.client.presenter.SummaryPresenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;

@GinModules({DispatchAsyncModule.class, MoraModule.class, SmartGWTViews.class})
public interface MoraGinjector extends Ginjector {

	PlaceManager getPlaceManager();

	EventBus getEventBus();

	ProxyFailureHandler getProxyFailureHandler();

	Provider<ErrorPresenter> getErrorPresenter();

	Provider<SummaryPresenter> getSummaryPresenter();
}