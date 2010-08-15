package sk.seges.acris.mvp.client.configuration.production;

import sk.seges.acris.mvp.client.configuration.AcrisModule;
import sk.seges.acris.mvp.client.configuration.smartgwt.SmartGWTViews;
import sk.seges.acris.mvp.client.presenter.ErrorPresenter;
import sk.seges.acris.mvp.client.presenter.UserMaintenancePresenter;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.philbeaudoin.gwtp.dispatch.client.gin.DefaultDispatchModule;
import com.philbeaudoin.gwtp.mvp.client.EventBus;
import com.philbeaudoin.gwtp.mvp.client.proxy.PlaceManager;
import com.philbeaudoin.gwtp.mvp.client.proxy.ProxyFailureHandler;

/**
 * MockGinjector should not extends AcrisGinjector. Following exception occurs:
 * 
 * If there is an inheritance between [ERROR] [sk.seges.acris.demo.MVP] The Ginjector
 * 'sk.seges.acris.mvp.client.configuration.mock.MockGinjector' does not have a get() method returning
 * 'Provider<UserMaintenancePresenter>'. This is required when using @ProxyStandard.
 * 
 * So, we have to report an issue to gwtp and fix that. This is only temporary solution.
 * 
 * @author fat
 */
@GinModules({DefaultDispatchModule.class, AcrisModule.class, ProductionModule.class, SmartGWTViews.class})
public interface ProductionGinjector extends Ginjector {

	PlaceManager getPlaceManager();

	EventBus getEventBus();

	ProxyFailureHandler getProxyFailureHandler();

	Provider<ErrorPresenter> getErrorPresenter();

	Provider<UserMaintenancePresenter> getUserMaintenancePresenter();
}