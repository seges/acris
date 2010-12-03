package sk.seges.acris.security.client;

import sk.seges.acris.security.client.event.LoginEvent;
import sk.seges.acris.security.client.event.OpenIDLoginEvent;
import sk.seges.acris.security.client.handler.LoginHandler;
import sk.seges.acris.security.client.handler.OpenIDLoginHandler;
import sk.seges.acris.security.client.i18n.LoginMessages;
import sk.seges.acris.security.client.presenter.OpenIDLoginPresenter;
import sk.seges.acris.security.client.presenter.OpenIDLoginPresenter.OpenIDLoginDisplay;
import sk.seges.acris.security.client.service.MockUserService;
import sk.seges.acris.security.client.view.OpenIDLoginView;
import sk.seges.acris.security.shared.IOpenIDConsumerService;
import sk.seges.acris.security.shared.IOpenIDConsumerServiceAsync;
import sk.seges.acris.security.shared.callback.SecuredAsyncCallback;
import sk.seges.acris.security.shared.exception.SecurityException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.UserPasswordLoginToken;
import sk.seges.acris.security.shared.user_management.service.UserServiceBroadcaster;
import sk.seges.acris.security.shared.user_management.service.UserServiceBroadcaster.BroadcastingException;
import sk.seges.acris.util.Pair;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Simple entry point with OpenID login dialog which sends session id to desired url on successful login.
 *  
 * @author Martin
 */
public class OpenID implements EntryPoint {

	private LoginMessages loginMessages = (LoginMessages) GWT.create(LoginMessages.class);

	private String redirectUrl = "http://www.google.sk";

	private OpenIDLoginPresenter loginPresenter;

	@SuppressWarnings("unchecked")
	@Override
	public void onModuleLoad() {
		// define a callback that receives a login result
		final AsyncCallback<ClientSession> callback = new SecuredAsyncCallback<ClientSession>() {

			@Override
			public void onSuccessCallback(ClientSession result) {
				if (result == null) {
					SimplePanel wrapper = new SimplePanel();
					wrapper.addStyleName("acris-login-panel-wrapper");
					wrapper.add(loginPresenter.getDisplay().asWidget());

					SimplePanel overlay = new SimplePanel();
					overlay.addStyleName("acris-login-overlay");

					RootPanel.get().add(overlay);
					RootPanel.get().add(wrapper);
				} else {
					handleSuccessfulLogin(result);
				}
			}

			public void onOtherException(Throwable e) {
				if (e instanceof BroadcastingException) {
					for (Throwable t : ((BroadcastingException) e).getCauses()) {
						GWT.log("Broadcaster received an error", t);
					}
				}
				GWT.log("Exception occured", e);
				handleFailedLogin();
			}

			public void onSecurityException(SecurityException e) {
				GWT.log("Security exception occured", e);
				handleFailedLogin();
			}
		};

		UserServiceBroadcaster broadcaster = new UserServiceBroadcaster();
		broadcaster.addUserService(new MockUserService());

		IOpenIDConsumerServiceAsync consumerService = GWT.create(IOpenIDConsumerService.class);
		((ServiceDefTarget) consumerService).setServiceEntryPoint("/ocs");

		OpenIDLoginDisplay display = GWT.create(OpenIDLoginView.class);

		loginPresenter = new OpenIDLoginPresenter(display, broadcaster, consumerService);

		Pair<String, String>[] languages = new Pair[2];
		languages[0] = new Pair<String, String>("sk", "slovensky");
		languages[1] = new Pair<String, String>("en", "english");

		boolean rememberMeAware = true;

		LoginHandler loginHandler = new LoginHandler() {

			@Override
			public void onSubmit(LoginEvent loginEvent) {
				String username = loginEvent.getUsername();
				String password = loginEvent.getPassword();
				String language = null;
				try {
					language = loginEvent.getLanguage().getFirst();
				} catch (Exception ignore) {
					// language support disabled - possible when extending this
					// class
				}

				UserPasswordLoginToken token = new UserPasswordLoginToken(username, password, language);
				loginPresenter.doLogin(token, callback);
			}
		};

		OpenIDLoginHandler openIDLoginHandler = new OpenIDLoginHandler() {

			@Override
			public void onSubmit(OpenIDLoginEvent openIDLoginEvent) {
				loginPresenter.authenticate(openIDLoginEvent.getIdentifier());
			}
		};

		loginPresenter.initDisplay(languages, rememberMeAware, loginHandler, openIDLoginHandler);

		loginPresenter.verify(callback);
	}

	private void handleFailedLogin() {
		Window.alert(loginMessages.loginFailedTitle());
		loginPresenter.onLoginFailed();
	}

	private void handleSuccessfulLogin(ClientSession result) {
		String query = "?";
		query += "sessionid=" + result.getSessionId();
		loginPresenter.doRedirect(redirectUrl + query);
	}
}
