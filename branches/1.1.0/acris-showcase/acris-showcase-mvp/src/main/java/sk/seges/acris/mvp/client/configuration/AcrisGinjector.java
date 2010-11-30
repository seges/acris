package sk.seges.acris.mvp.client.configuration;

import sk.seges.acris.mvp.client.configuration.smartgwt.SmartGWTViews;
import sk.seges.acris.mvp.client.presenter.user.LoginPresenter;
import sk.seges.acris.mvp.client.presenter.user.UserMaintenancePresenter;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.showcase.client.presenter.core.ErrorPresenter;

import com.google.gwt.event.shared.EventBus;
import com.google.gwt.inject.client.GinModules;
import com.google.gwt.inject.client.Ginjector;
import com.google.inject.Provider;
import com.gwtplatform.mvp.client.proxy.PlaceManager;
import com.gwtplatform.mvp.client.proxy.ProxyFailureHandler;

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