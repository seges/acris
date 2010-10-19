package sk.seges.acris.mvp.client.configuration;

import sk.seges.acris.mvp.client.configuration.smartgwt.SmartGWTViews;
import sk.seges.acris.mvp.client.presenter.core.ErrorPresenter;
import sk.seges.acris.mvp.client.presenter.user.LoginPresenter;
import sk.seges.acris.mvp.client.presenter.user.UserMaintenancePresenter;
import sk.seges.acris.security.shared.session.ClientSession;

import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.philbeaudoin.gwtp.dispatch.client.gin.DefaultDispatchModule;
import com.philbeaudoin.gwtp.mvp.client.EventBus;
import com.philbeaudoin.gwtp.mvp.client.proxy.PlaceManager;
import com.philbeaudoin.gwtp.mvp.client.proxy.ProxyFailureHandler;

@GinModules({DefaultDispatchModule.class, AcrisModule.class, SmartGWTViews.class})
public interface AcrisGinjector extends Ginjector {

	ClientSession getClientSession();

	PlaceManager getPlaceManager();

	EventBus getEventBus();

	ProxyFailureHandler getProxyFailureHandler();

	Provider<ErrorPresenter> getErrorPresenter();

	Provider<LoginPresenter> getLoginPresenter();

	Provider<UserMaintenancePresenter> getUserMaintenancePresenter();
}