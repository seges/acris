package sk.seges.acris.generator.client.gin.production;

import sk.seges.acris.generator.client.gin.common.MoraModule;
import sk.seges.acris.generator.client.gin.smartgwt.SmartGWTViews;
import sk.seges.acris.generator.client.presenter.SummaryPresenter;
import sk.seges.acris.generator.shared.service.IGeneratorServiceAsync;
import sk.seges.acris.showcase.client.presenter.core.ErrorPresenter;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.EventBus;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;

@GinModules({DispatchAsyncModule.class, MoraModule.class, SmartGWTViews.class})
public interface MoraGinjector extends Ginjector {

	PlaceManager getPlaceManager();

	IGeneratorServiceAsync getGeneratorService();
	
	EventBus getEventBus();

	ProxyFailureHandler getProxyFailureHandler();

	Provider<ErrorPresenter> getErrorPresenter();

	Provider<SummaryPresenter> getSummaryPresenter();
}