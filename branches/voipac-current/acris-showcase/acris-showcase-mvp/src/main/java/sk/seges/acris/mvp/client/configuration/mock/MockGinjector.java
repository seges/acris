package sk.seges.acris.mvp.client.configuration.mock;

import sk.seges.acris.mvp.client.action.mocks.MockAddUserActionHandler;
import sk.seges.acris.mvp.client.action.mocks.MockFetchUsersActionHandler;
import sk.seges.acris.mvp.client.configuration.AcrisModule;
import sk.seges.acris.mvp.client.configuration.security.SecuredDispatchModule;
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

@GinModules({SecuredDispatchModule.class, AcrisModule.class, MockModule.class, SmartGWTViews.class})
public interface MockGinjector extends Ginjector {

	MockAddUserActionHandler getAddUserActionHandler();

	MockFetchUsersActionHandler getFetchUsersActionHandler();

	ClientSession getClientSession();

	PlaceManager getPlaceManager();

	EventBus getEventBus();

	ProxyFailureHandler getProxyFailureHandler();

	Provider<ErrorPresenter> getErrorPresenter();

	Provider<LoginPresenter> getLoginPresenter();

	Provider<UserMaintenancePresenter> getUserMaintenancePresenter();

}