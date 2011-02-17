package sk.seges.acris.generator.client.gin.production;

import sk.seges.acris.generator.client.gin.common.GeneratorShowcaseModule;
import sk.seges.acris.generator.client.presenter.SummaryOfflinePresenter;
import sk.seges.acris.generator.shared.service.IGeneratorServiceAsync;
import sk.seges.acris.showcase.client.presenter.core.ErrorPresenter;
import sk.seges.acris.showcase.mora.client.gin.smartgwt.SmartGWTViews;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;

@GinModules({DispatchAsyncModule.class, GeneratorShowcaseModule.class, SmartGWTViews.class})
public interface MoraGinjector extends Ginjector {

	PlaceManager getPlaceManager();

	IGeneratorServiceAsync getGeneratorService();
	
	EventBus getEventBus();

	ProxyFailureHandler getProxyFailureHandler();

	Provider<ErrorPresenter> getErrorPresenter();

	Provider<SummaryOfflinePresenter> getSummaryPresenter();
}