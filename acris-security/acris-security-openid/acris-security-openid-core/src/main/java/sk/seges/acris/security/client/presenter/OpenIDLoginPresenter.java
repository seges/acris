package sk.seges.acris.security.client.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.seges.acris.common.util.Pair;
import sk.seges.acris.security.client.event.OpenIDLoginEvent;
import sk.seges.acris.security.client.handler.HasOpenIDLoginHandlers;
import sk.seges.acris.security.client.handler.OpenIDLoginHandler;
import sk.seges.acris.security.client.openid.configuration.DefaultOpenIdConfiguration;
import sk.seges.acris.security.client.openid.configuration.OpenIdConfiguration;
import sk.seges.acris.security.client.presenter.LoginPresenter.LoginDisplay;
import sk.seges.acris.security.client.presenter.OpenIDLoginPresenter.OpenIDLoginDisplay;
import sk.seges.acris.security.shared.callback.SecuredAsyncCallback;
import sk.seges.acris.security.shared.configuration.LoginConfiguration;
import sk.seges.acris.security.shared.dto.OpenIDUserDTO;
import sk.seges.acris.security.shared.exception.SecurityException;
import sk.seges.acris.security.shared.service.IOpenIDConsumerRemoteServiceAsync;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.OpenIDLoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;
import sk.seges.acris.security.shared.user_management.service.UserServiceBroadcaster;
import sk.seges.acris.security.shared.user_management.service.UserServiceBroadcaster.BroadcastingException;
import sk.seges.acris.security.shared.util.LoginUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;

public class OpenIDLoginPresenter extends LoginPresenter<OpenIDLoginDisplay> implements HasOpenIDLoginHandlers {

	public interface OpenIDLoginDisplay extends LoginDisplay {

		HandlerRegistration addOpenIDButtonHandler(ClickHandler handler, String style);
	}

	protected IOpenIDConsumerRemoteServiceAsync consumerService;

	protected ClientSession<OpenIDUserDTO> clientSession;
	
	protected LoginConfiguration loginConfiguration = GWT.create(LoginConfiguration.class);
	protected OpenIdConfiguration openIdConfiguration;

	public OpenIDLoginPresenter(OpenIDLoginDisplay display, UserServiceBroadcaster broadcaster, String redirectUrl,
			IOpenIDConsumerRemoteServiceAsync consumerService) {
		this(display, broadcaster, redirectUrl, null, false, consumerService, null, null);
	}

	public OpenIDLoginPresenter(OpenIDLoginDisplay display, UserServiceBroadcaster broadcaster, String redirectUrl,
			IOpenIDConsumerRemoteServiceAsync consumerService, String locale) {
		this(display, broadcaster, redirectUrl, null, false, consumerService, null, locale);
	}

	public OpenIDLoginPresenter(OpenIDLoginDisplay display, UserServiceBroadcaster broadcaster, String redirectUrl,
			Pair<String, String>[] enabledLanguages, boolean rememberMeEnabled,
			IOpenIDConsumerRemoteServiceAsync consumerService, ClientSession<OpenIDUserDTO> clientSession, String locale) {
		super(display, broadcaster, redirectUrl, enabledLanguages, rememberMeEnabled, locale);
		this.consumerService = consumerService;
		this.clientSession = clientSession;
	}

	@Override
	public HandlerRegistration addOpenIDLoginHandler(OpenIDLoginHandler handler) {
		return addHandler(OpenIDLoginEvent.getType(), handler);
	}

	@Override
	public void bind(final HasWidgets parent) {
		setLocaleFromCookies();

		AsyncCallback<String> stringCallback = null;
		AsyncCallback<ClientSession<OpenIDUserDTO>> clientCallback = null;

		if (redirectUrl != null) {
			stringCallback = new SecuredAsyncCallback<String>() {

				@Override
				public void onSuccessCallback(String result) {
					if (result != null) {
						ClientSession<OpenIDUserDTO> resultSession = new ClientSession<OpenIDUserDTO>();
						resultSession.setSessionId(result);
						handleSuccessfulLogin(resultSession);
					} else {
						registerHandlers();
						registerLoginHandlers(this);
						superBind(parent);
						readLoginCookies();
					}
				}

				@Override
				public void onOtherException(Throwable e) {
					if (e instanceof BroadcastingException) {
						for (Throwable t : ((BroadcastingException) e).getCauses()) {
							GWT.log("Broadcaster received an error", t);
						}
					} else {
						GWT.log("Exception occured", e);
					}
					handleFailedLogin();
				}

				@Override
				public void onSecurityException(SecurityException e) {
					GWT.log("Security exception occured", e);
					handleFailedLogin();
				}
			};
		} else {
			clientCallback = new SecuredAsyncCallback<ClientSession<OpenIDUserDTO>>() {

				@Override
				public void onSuccessCallback(ClientSession<OpenIDUserDTO> result) {
					if (result != null) {
						handleSuccessfulLogin(result);
					} else {
						superBind(parent);
						readLoginCookies();
						registerHandlers();
						registerLoginHandlers(this);
					}
				}

				@Override
				public void onOtherException(Throwable e) {
					if (e instanceof BroadcastingException) {
						for (Throwable t : ((BroadcastingException) e).getCauses()) {
							GWT.log("Broadcaster received an error", t);
						}
					} else {
						GWT.log("Exception occured", e);
					}
					handleFailedLogin();
				}

				@Override
				public void onSecurityException(SecurityException e) {
					GWT.log("Security exception occured", e);
					handleFailedLogin();
				}
			};
		}

		verify(stringCallback != null ? stringCallback : clientCallback);
	}

	@Override
	protected void registerHandlers() {
		super.registerHandlers();

		registerHandler(display.addOpenIDButtonHandler(createButtonHandler(OpenIDProvider.GOOGLE.getIdentifier()),
				"acris-login-google-button"));

		registerHandler(display.addOpenIDButtonHandler(createButtonHandler(OpenIDProvider.YAHOO.getIdentifier()),
				"acris-login-yahoo-button"));

		registerHandler(display.addOpenIDButtonHandler(createButtonHandler(OpenIDProvider.AOL.getIdentifier()),
				"acris-login-aol-button"));

		registerHandler(display.addOpenIDButtonHandler(createButtonHandler(OpenIDProvider.SEZNAM.getIdentifier()),
				"acris-login-seznam-button"));
		
//		registerHandler(display.addOpenIDButtonHandler(createButtonHandler(LoginConstants.MYOPENID_IDENTIFIER),
//				"acris-login-myopenid-button"));
	}

	@Override
	protected void registerLoginHandlers(AsyncCallback<?> callback) {
		super.registerLoginHandlers(callback);

		OpenIDLoginHandler openIDLoginHandler = new OpenIDLoginHandler() {

			@Override
			public void onSubmit(OpenIDLoginEvent openIDLoginEvent) {
				authenticate(openIDLoginEvent.getIdentifier());
			}
		};

		registerHandler(addOpenIDLoginHandler(openIDLoginHandler));
	}

	protected OpenIDLoginToken constructOpenIDLoginToken(String identifier, String email, OpenIDProvider provider, String webId) {
		return new OpenIDLoginToken(identifier, email, provider, webId, locale);
	}

	protected OpenIdConfiguration getOpenIdConfiguration() {
		if (openIdConfiguration == null) {
			openIdConfiguration = new DefaultOpenIdConfiguration();
		}
		
		return openIdConfiguration;
	}
	
	/**
	 * Authenticates an OpenID identifier and opens the discovered provider's
	 * endpoint url in a popup.
	 * 
	 * @param identifier
	 */
	protected void authenticate(final String identifier) {
		consumerService.authenticate(identifier, getReturnURL(), getOpenIdConfiguration().getRealm()/*loginConfiguration.getLoginModule()*/,
				new AsyncCallback<OpenIDUserDTO>() {

					@Override
					public void onFailure(Throwable caught) {
						// Cookies.removeCookie(LoginConstants.OPENID_COOKIE_NAME);
						GWT.log("Authentication failed", caught);
						display.showMessage("Authentication failed, exception: " + caught.getLocalizedMessage());
					}

					@Override
					public void onSuccess(OpenIDUserDTO result) {
						if (result != null) {
							// Cookies.setCookie(LoginConstants.OPENID_COOKIE_NAME,
							// identifier);

							if (clientSession != null) {
								clientSession.setSessionId(result.getSessionId());
							}

							String url = result.getEndpointUrl();
							Window.open(url, "openIDPopup", "width = 500," + "height = 500," + "left = 200,"
									+ "top = 200," + "resizable = yes," + "scrollbars = no," + "status = no,"
									+ "toolbar = no");
						} else {
							// Cookies.removeCookie(LoginConstants.OPENID_COOKIE_NAME);
							GWT.log("Authentication failed, OpenID provider not found");
							display.showMessage("Authentication failed, OpenID provider not found");
						}
					}
				});
	}

	/**
	 * Verifies a response from an OpenID provider and calls login on
	 * broadcaster.
	 * 
	 * @param callback
	 */
	protected void verify(final AsyncCallback<?> callback) {
		final String mode = Location.getParameter("openid.mode");

		if (mode != null) {
			if (mode.equals("cancel") || mode.equals("setup_needed")) {
				// Cookies.removeCookie(LoginConstants.OPENID_COOKIE_NAME);
				closePopup(null);
			} else if (mode.equals("id_res") && Window.Location.getParameter("openid.response_nonce") != null) {
				final String href = Window.Location.getHref();
				final Map<String, List<String>> parameterMap = Window.Location.getParameterMap();
				final Map<String, String[]> map = new HashMap<String, String[]>();
				Set<String> keys = parameterMap.keySet();

				for (String key : keys) {
					List<String> value = parameterMap.get(key);
					map.put(key, (String[]) value.toArray(new String[value.size()]));
				}

				display.displayMessage(loginMessages.loginProgress());

				consumerService.verify(href, map, new AsyncCallback<OpenIDUserDTO>() {

					@Override
					public void onFailure(Throwable caught) { //
						// Cookies.removeCookie(LoginConstants.OPENID_COOKIE_NAME);
						unbind();
						closePopup(null);
						callback.onFailure(new sk.seges.acris.security.shared.exception.SecurityException(
								"Failed to log in user locally"));
					}

					@Override
					public void onSuccess(OpenIDUserDTO result) {
						doLogin(constructOpenIDLoginToken(result.getOpenIDIdentifier(), result.getEmail(),
								getProviderFromURL(href), null), callback);
					}
				});
			}
		} else {
			// auto login
			// String identifier =
			// Cookies.getCookie(LoginConstants.OPENID_COOKIE_NAME);
			// if (identifier != null) {
			// authenticate(identifier);
			// }
			callback.onSuccess(null);
		}
	}

	@Override
	protected void handleFailedLogin() {
		super.handleFailedLogin();
		closePopup(null);
	}

	@Override
	protected void handleSuccessfulLogin(ClientSession<?> result) {
		display.setUsername("");
		display.setPassword("");

		unbind();

		if (redirectUrl != null && !redirectUrl.isEmpty()) {
			String sessionId = result.getSessionId();
			String query = LoginUtils.getCommonQueryString(sessionId);

			closePopup(redirectUrl != null ? redirectUrl + query : null);

			RootPanel.get().clear();
			Location.replace(redirectUrl + query);
		}
	}

	private OpenIDProvider getProviderFromURL(String url) {
		if (url.contains("google.com")) {
			return OpenIDProvider.GOOGLE;
		} else if (url.contains("yahoo.com")) {
			return OpenIDProvider.YAHOO;
		} else if (url.contains("seznam.cz")) {
			return OpenIDProvider.SEZNAM;
		} else if (url.contains("myopenid.com")) {
			return OpenIDProvider.MYOPENID;
		} else if (url.contains("aol.com")) {
			return OpenIDProvider.AOL;
		}

		return null;
	}

	private ClickHandler createButtonHandler(final String identifier) {

		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new OpenIDLoginEvent(identifier));
			}
		};
	}

	private String getReturnURL() {
		return Window.Location.getHref();
	}

	private native void closePopup(String url) /*-{
		if ($wnd.name == 'openIDPopup') {
			$wnd.close();
			if (url && $wnd.opener && !$wnd.opener.closed) {
				$wnd.opener.location.replace(url);
			}
		}
	}-*/;
}
