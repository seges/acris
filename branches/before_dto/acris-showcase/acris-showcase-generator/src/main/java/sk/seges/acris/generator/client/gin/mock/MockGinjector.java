package sk.seges.acris.generator.client.gin.mock;

import sk.seges.acris.generator.client.action.mocks.MockGenerateOfflineActionHandler;
import sk.seges.acris.generator.client.gin.common.GeneratorShowcaseModule;
import sk.seges.acris.generator.client.presenter.SummaryOfflinePresenter;
import sk.seges.acris.generator.shared.service.IGeneratorServiceAsync;
import sk.seges.acris.showcase.client.presenter.core.ErrorPresenter;
import sk.seges.acris.showcase.mora.client.action.mocks.MockFetchMoviesActionHandler;
import sk.seges.acris.showcase.mora.client.action.mocks.MockSaveRatingActionHandler;
import sk.seges.acris.showcase.mora.client.gin.smartgwt.SmartGWTViews;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;

/**
 * MockGinjector should not extends AcrisGinjector. Following exception occurs: If there is an inheritance between
 * [ERROR] [sk.seges.acris.demo.MVP] The Ginjector 'sk.seges.acris.mvp.client.configuration.mock.MockGinjector' does not
 * have a get() method returning 'Provider<UserMaintenancePresenter>'. This is required when using @ProxyStandard. So,
 * we have to report an issue to gwtp and fix that. This is only temporary solution.
 * 
 * @author fat
 */

@GinModules({DispatchAsyncModule.class, GeneratorShowcaseModule.class, MockModule.class, SmartGWTViews.class})
public interface MockGinjector extends Ginjector {

	MockFetchMoviesActionHandler getFetchMoviesActionHandler();

	MockGenerateOfflineActionHandler getGenerateOfflineActionHandler();

	MockSaveRatingActionHandler getSaveRatingActionHandler();

	IGeneratorServiceAsync getGeneratorService();

	PlaceManager getPlaceManager();

	EventBus getEventBus();

	ProxyFailureHandler getProxyFailureHandler();

	Provider<ErrorPresenter> getErrorPresenter();

	Provider<SummaryOfflinePresenter> getSummaryPresenter();
}