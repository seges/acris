package sk.seges.acris.security.client.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import sk.seges.acris.security.shared.user_management.service.UserServiceBroadcaster;
import sk.seges.acris.security.shared.user_management.service.UserServiceBroadcaster.BroadcastingException;
import sk.seges.acris.security.shared.util.LoginConstants;
import sk.seges.acris.util.Pair;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;

public class OpenIDLoginPresenter extends LoginPresenter<OpenIDLoginDisplay> {

	public interface OpenIDLoginDisplay extends LoginDisplay, HasOpenIDLoginHandlers {

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

		AsyncCallback<ClientSession> securedCallback = new SecuredAsyncCallback<ClientSession>() {

			@Override
			public void onSuccessCallback(ClientSession result) {
				if (result != null) {
					handleSuccessfulLogin(result);
				} else {
					superBind(parent);

					readLoginCookies();
					registerHandlers(this);
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

		verify(securedCallback);
	}

	@Override
	protected void registerHandlers(AsyncCallback<ClientSession> securedCallback) {
		super.registerHandlers(securedCallback);

		OpenIDLoginHandler openIDLoginHandler = new OpenIDLoginHandler() {

			@Override
			public void onSubmit(OpenIDLoginEvent openIDLoginEvent) {
				authenticate(openIDLoginEvent.getIdentifier());
			}
		};

		registerHandler(display.addOpenIDLoginHandler(openIDLoginHandler));

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

	/**
	 * Authenticates an OpenID identifier and opens the discovered provider's
	 * login url in a popup.
	 * 
	 * @param identifier
	 */
	protected void authenticate(String identifier) {
		consumerService.authenticate(identifier, getModuleUrl(), new AsyncCallback<OpenIDUser>() {

			@Override
			public void onSuccess(OpenIDUser result) {
				if (result != null) {
					String url = result.getRedirectUrl();
					url += "&openid.ns.ui=" + URL.encodeQueryString("http://specs.openid.net/extensions/ui/1.0");
					url += "&openid.ui.mode=" + URL.encodeQueryString("popup");
					Window.open(url, "openIDPopup", "width = 500," + "height = 540," + "left = 200," + "top = 200,"
							+ "resizable = yes," + "scrollbars = no," + "status = no," + "toolbar = no");
				} else {
					display.showMessage("Authentication failed, OpenID provider not found");
				}
			}

			@Override
			public void onFailure(Throwable caught) {
				display.showMessage("Authentication failed, exception: " + caught.getLocalizedMessage());
			}
		});
	}

	/**
	 * Verifies a response from an OpenID provider and calls login on
	 * broadcaster.
	 * 
	 * @param callback
	 */
	protected void verify(final AsyncCallback<ClientSession> callback) {
		final String mode = Window.Location.getParameter("openid.mode");

		if (mode != null) {
			if (mode.equals("cancel")) {
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
						unbind();
						closePopup(null);
						callback.onFailure(new sk.seges.acris.security.shared.exception.SecurityException(
								"Failed to log in user locally"));
					}

					@Override
					public void onSuccess(String result) {
						OpenIDLoginToken token = new OpenIDLoginToken(result);
						doLogin(token, callback);
					}
				});
			}
		} else {
			callback.onSuccess(null);
		}
	}

	@Override
	protected void doRedirect(String redirectUrl) {
		closePopup(redirectUrl);
		super.doRedirect(redirectUrl);
	}

	private ClickHandler createButtonHandler(final String identifier) {

		return new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				display.fireEvent(new OpenIDLoginEvent(identifier));
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
}
