package sk.seges.acris.security.client.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.seges.acris.common.util.Pair;
import sk.seges.acris.security.client.event.OpenIDLoginEvent;
import sk.seges.acris.security.client.handler.HasOpenIDLoginHandlers;
import sk.seges.acris.security.client.handler.OpenIDLoginHandler;
import sk.seges.acris.security.client.presenter.LoginPresenter.LoginDisplay;
import sk.seges.acris.security.client.presenter.OpenIDLoginPresenter.OpenIDLoginDisplay;
import sk.seges.acris.security.shared.callback.SecuredAsyncCallback;
import sk.seges.acris.security.shared.data.OpenIDUser;
import sk.seges.acris.security.shared.exception.SecurityException;
import sk.seges.acris.security.shared.service.IOpenIDConsumerServiceAsync;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.OpenIDLoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.OpenIDProvider;
import sk.seges.acris.security.shared.user_management.service.UserServiceBroadcaster;
import sk.seges.acris.security.shared.user_management.service.UserServiceBroadcaster.BroadcastingException;
import sk.seges.acris.security.shared.util.LoginConstants;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;

public class OpenIDLoginPresenter extends LoginPresenter<OpenIDLoginDisplay> implements HasOpenIDLoginHandlers {

	public interface OpenIDLoginDisplay extends LoginDisplay {

		HandlerRegistration addOpenIDButtonHandler(ClickHandler handler, String style);
	}

	protected IOpenIDConsumerServiceAsync consumerService;

	public OpenIDLoginPresenter(OpenIDLoginDisplay display, UserServiceBroadcaster broadcaster, String redirectUrl,
			IOpenIDConsumerServiceAsync consumerService) {
		this(display, broadcaster, redirectUrl, null, false, consumerService);
	}

	public OpenIDLoginPresenter(OpenIDLoginDisplay display, UserServiceBroadcaster broadcaster, String redirectUrl,
			Pair<String, String>[] enabledLanguages, boolean rememberMeEnabled,
			IOpenIDConsumerServiceAsync consumerService) {
		super(display, broadcaster, redirectUrl, enabledLanguages, rememberMeEnabled);
		this.consumerService = consumerService;
	}

	@Override
	public void bind(final HasWidgets parent) {
		setLocaleFromCookies();

		AsyncCallback<String> stringCallback = null;
		AsyncCallback<ClientSession> clientCallback = null;

		if (redirectUrl != null) {
			stringCallback = new SecuredAsyncCallback<String>() {

				@Override
				public void onSuccessCallback(String result) {
					if (result != null) {
						ClientSession resultSession = new ClientSession();
						resultSession.setSessionId(result);
						handleSuccessfulLogin(resultSession);
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
		} else {
			clientCallback = new SecuredAsyncCallback<ClientSession>() {

				@Override
				public void onSuccessCallback(ClientSession result) {
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

		registerHandler(display.addOpenIDButtonHandler(createButtonHandler(LoginConstants.GOOGLE_IDENTIFIER),
				"acris-login-google-button"));

		registerHandler(display.addOpenIDButtonHandler(createButtonHandler(LoginConstants.YAHOO_IDENTIFIER),
				"acris-login-yahoo-button"));

		registerHandler(display.addOpenIDButtonHandler(createButtonHandler(LoginConstants.AOL_IDENTIFIER),
				"acris-login-aol-button"));

		registerHandler(display.addOpenIDButtonHandler(createButtonHandler(LoginConstants.SEZNAM_IDENTIFIER),
				"acris-login-seznam-button"));

		registerHandler(display.addOpenIDButtonHandler(createButtonHandler(LoginConstants.MYOPENID_IDENTIFIER),
				"acris-login-myopenid-button"));
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

	/**
	 * Authenticates an OpenID identifier and opens the discovered provider's
	 * endpoint url in a popup.
	 * 
	 * @param identifier
	 */
	protected void authenticate(final String identifier) {
		consumerService.authenticate(identifier, getModuleUrl(), new AsyncCallback<OpenIDUser>() {

			@Override
			public void onFailure(Throwable caught) {
				// Cookies.removeCookie(LoginConstants.OPENID_COOKIE_NAME);
				display.showMessage("Authentication failed, exception: " + caught.getLocalizedMessage());
			}

			@Override
			public void onSuccess(OpenIDUser result) {
				if (result != null) {
					// Cookies.setCookie(LoginConstants.OPENID_COOKIE_NAME,
					// identifier);
					String url = result.getRedirectUrl();
					url += "&openid.ns.ui=" + URL.encodeQueryString("http://specs.openid.net/extensions/ui/1.0");
					url += "&openid.ui.mode=" + URL.encodeQueryString("popup");
					Window.open(url, "openIDPopup", "width = 500," + "height = 500," + "left = 200," + "top = 200,"
							+ "resizable = yes," + "scrollbars = no," + "status = no," + "toolbar = no");
				} else {
					// Cookies.removeCookie(LoginConstants.OPENID_COOKIE_NAME);
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
				consumerService.verify(href, map, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// Cookies.removeCookie(LoginConstants.OPENID_COOKIE_NAME);
						unbind();
						closePopup(null);
						callback.onFailure(new sk.seges.acris.security.shared.exception.SecurityException(
								"Failed to log in user locally"));
					}

					@Override
					public void onSuccess(String result) {
						OpenIDLoginToken token = new OpenIDLoginToken(result);
						String email = parameterMap.get("openid.ext1.value.email").get(0);
						token.setEmail(email);
						token.setProvider(OpenIDProvider.GOOGLE);
						doLogin(token, callback);
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
	protected void handleSuccessfulLogin(ClientSession result) {
		display.setUsername("");
		display.setPassword("");

		unbind();

		String query = "";
		if (redirectUrl != null) {
			if (GWT.isProdMode()) {
				query = "?" + LoginConstants.ACRIS_SESSION_ID_STRING + "=" + result.getSessionId();
			} else {
				query = "?gwt.codesvr=127.0.0.1:9997&" + LoginConstants.ACRIS_SESSION_ID_STRING + "="
						+ result.getSessionId();
			}

			closePopup(redirectUrl != null ? redirectUrl + query : null);

			RootPanel.get().clear();
			Location.replace(redirectUrl + query);
		}
	}

	private ClickHandler createButtonHandler(final String identifier) {

		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireEvent(new OpenIDLoginEvent(identifier));
			}
		};
	}

	private String getModuleUrl() {
		return Window.Location.getHref();
	}

	private native void closePopup(String url) /*-{
												if ($wnd.name == 'openIDPopup')
												{
												$wnd.close();
												if (url && $wnd.opener && !$wnd.opener.closed)
												{
												$wnd.opener.location.replace(url);
												}
												}
												}-*/;

	@Override
	public HandlerRegistration addOpenIDLoginHandler(OpenIDLoginHandler handler) {
		return addHandler(OpenIDLoginEvent.getType(), handler);
	}
}
