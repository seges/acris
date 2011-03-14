package sk.seges.acris.security.client.presenter;

import sk.seges.acris.common.util.Pair;
import sk.seges.acris.common.util.URLUtils;
import sk.seges.acris.security.client.event.LoginEvent;
import sk.seges.acris.security.client.handler.HasLoginHandlers;
import sk.seges.acris.security.client.handler.LoginHandler;
import sk.seges.acris.security.client.i18n.LoginMessages;
import sk.seges.acris.security.client.presenter.LoginPresenter.LoginDisplay;
import sk.seges.acris.security.shared.callback.SecuredAsyncCallback;
import sk.seges.acris.security.shared.exception.SecurityException;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.UserPasswordLoginToken;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.service.IUserServiceAsync;
import sk.seges.acris.security.shared.user_management.service.UserServiceBroadcaster.BroadcastingException;
import sk.seges.acris.security.shared.util.LoginConstants;
import sk.seges.acris.security.shared.util.LoginUtils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window.Location;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.RootPanel;

public class LoginPresenter<D extends LoginDisplay> extends BasePresenter<D> implements HasLoginHandlers {

	public interface LoginDisplay extends BaseDisplay {

		HandlerRegistration addLoginButtonHandler(ClickHandler handler);

		HandlerRegistration addUsernameKeyHandler(KeyUpHandler handler);

		HandlerRegistration addPasswordKeyHandler(KeyUpHandler handler);

		HandlerRegistration addUsernameChangeHandler(ChangeHandler handler);

		HandlerRegistration addPasswordChangeHandler(ChangeHandler handler);

		HandlerRegistration addLanguageHandler(ChangeHandler handler);

		void onLoginFailed();

		void setEnabledLanguages(Pair<String, String>[] languages);

		Pair<String, String> getSelectedLanguage();

		void setSelectedLanguage(String language);

		void setRememberMeEnabled(boolean enabled);

		boolean getRememberMe();

		void setRemeberMe(boolean rememberMe);

		boolean isLoginEnabled();

		void setLoginEnabled(boolean enabled);

		void updateLoginEnabled();

		String getUsername();

		void setUsername(String name);

		String getPassword();

		void setPassword(String pass);
	}

	protected IUserServiceAsync broadcaster;

	protected String redirectUrl;

	protected Pair<String, String>[] enabledLanguages;

	protected boolean rememberMeEnabled;

	protected LoginMessages loginMessages = (LoginMessages) GWT.create(LoginMessages.class);

	/**
	 * Default validator that simply checks if the username and password are not
	 * empty.
	 */
	protected LoginValidator validator = new LoginValidator() {

		public boolean validateUsername(final String username) {
			return username.trim().length() > 0;
		}

		public boolean validatePassword(final String password) {
			return password.trim().length() > 0;
		}
	};

	/**
	 * Sets the callback to validate the username and password fields. If these
	 * fields fail validation the login button will be disabled.
	 */
	public void setValidator(LoginValidator validator) {
		if (validator == null) {
			throw new IllegalArgumentException("LoginValidator cannot be set to null");
		}
		this.validator = validator;
	}

	public LoginPresenter(D display, IUserServiceAsync broadcaster, String redirectUrl) {
		this(display, broadcaster, redirectUrl, null, false);
	}

	public LoginPresenter(D display, IUserServiceAsync broadcaster, String redirectUrl,
			Pair<String, String>[] enabledLanguages, boolean rememberMeEnabled) {
		super(display);

		this.broadcaster = broadcaster;
		this.redirectUrl = redirectUrl;
		this.enabledLanguages = enabledLanguages;
		this.rememberMeEnabled = rememberMeEnabled;

		display.setEnabledLanguages(enabledLanguages);
		display.setRememberMeEnabled(rememberMeEnabled);
	}

	@Override
	public HandlerRegistration addLoginHandler(LoginHandler handler) {
		return addHandler(LoginEvent.getType(), handler);
	}
	
	@Override
	public void bind(final HasWidgets parent) {
		setLocaleFromCookies();
		registerHandlers();
		superBind(parent);
		readLoginCookies();

		AsyncCallback<String> stringCallback = null;
		AsyncCallback<ClientSession> clientCallback = null;

		if (redirectUrl != null && !redirectUrl.isEmpty()) {
			stringCallback = new SecuredAsyncCallback<String>() {

				@Override
				public void onSuccessCallback(String result) {
					if (result != null) {
						ClientSession resultSession = new ClientSession();
						resultSession.setSessionId(result);
						handleSuccessfulLogin(resultSession);
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

		registerLoginHandlers(stringCallback != null ? stringCallback : clientCallback);
	}

	protected void setLocaleFromCookies() {
		String language = Cookies.getCookie(LoginConstants.LANGUAGE_COOKIE_NAME);
		String localeName = LocaleInfo.getCurrentLocale().getLocaleName();
		if (language != null && !language.isEmpty() && localeName != null && !localeName.isEmpty()
				&& !language.equals(localeName)) {
			String url = URLUtils.transformURLToRequiredLocale(Location.getHref(), Location.getHostName(), null,
					localeName, language);
			if (!Location.getHref().equals(url)) {
				unbind();
				RootPanel.get().clear();
				Location.assign(url);
			}
		}
	}

	protected void superBind(HasWidgets parent) {
		super.bind(parent);
	}

	protected void fireLoginEvent() {
		if (rememberMeEnabled) {
			if (Boolean.TRUE.equals(display.getRememberMe())) {
				Cookies.setCookie(LoginConstants.LOGINNAME_COOKIE_NAME, display.getUsername());
				Cookies.setCookie(LoginConstants.LOGINPASSWORD_COOKIE_NAME, display.getPassword());
				try {
					Cookies.setCookie(LoginConstants.LANGUAGE_COOKIE_NAME, display.getSelectedLanguage().getFirst());
				} catch (RuntimeException ignored) {
					// language support disabled
				}
			}
		}
		display.setLoginEnabled(false);
		fireEvent(new LoginEvent(display.getUsername(), display.getPassword(),
				(enabledLanguages != null ? display.getSelectedLanguage() : null)));
	}

	protected void updateLoginEnabled() {
		display.setLoginEnabled(validator.validateUsername(display.getUsername())
				&& validator.validatePassword(display.getPassword()));
		display.updateLoginEnabled();
	}

	protected void readLoginCookies() {
		String language = Cookies.getCookie(LoginConstants.LANGUAGE_COOKIE_NAME);
		String loginName = Cookies.getCookie(LoginConstants.LOGINNAME_COOKIE_NAME);
		String loginPassword = Cookies.getCookie(LoginConstants.LOGINPASSWORD_COOKIE_NAME);

		display.setSelectedLanguage(language);

		if (loginName != null && !loginName.isEmpty() && loginPassword != null && !loginPassword.isEmpty()) {
			display.setUsername(loginName);
			display.setPassword(loginPassword);
			if (rememberMeEnabled) {
				display.setRemeberMe(true);
			}
			fireLoginEvent();
		}
	}

	protected LoginToken constructLoginToken(LoginEvent loginEvent) {
		String username = loginEvent.getUsername();
		String password = loginEvent.getPassword();
		String language = null;
		try {
			language = loginEvent.getLanguage().getFirst();
		} catch (Exception ignore) {
			// language support disabled
		}

		return new UserPasswordLoginToken(username, password, language);
	}

	protected void registerHandlers() {
		registerHandler(display.addLoginButtonHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				fireLoginEvent();
			}
		}));

		KeyUpHandler keyHandler = new KeyUpHandler() {

			@Override
			public void onKeyUp(KeyUpEvent event) {
				updateLoginEnabled();
				if (event.getNativeEvent().getKeyCode() == KeyCodes.KEY_ENTER) {
					if (display.isLoginEnabled()) {
						fireLoginEvent();
					}
				}
			}
		};

		registerHandler(display.addUsernameKeyHandler(keyHandler));

		registerHandler(display.addPasswordKeyHandler(keyHandler));

		ChangeHandler changeHandler = new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				updateLoginEnabled();
			}
		};

		registerHandler(display.addUsernameChangeHandler(changeHandler));

		registerHandler(display.addPasswordChangeHandler(changeHandler));

		if (enabledLanguages != null) {
			registerHandler(display.addLanguageHandler(new ChangeHandler() {

				@Override
				public void onChange(ChangeEvent event) {
					String selectedLanguage = display.getSelectedLanguage().getFirst();
					display.setSelectedLanguage(selectedLanguage);
					Cookies.setCookie(LoginConstants.LANGUAGE_COOKIE_NAME, selectedLanguage);
					unbind();
					RootPanel.get().clear();
					Location.assign(URLUtils.transformURLToRequiredLocale(Location.getHref(), Location.getHostName(),
							null, LocaleInfo.getCurrentLocale().getLocaleName(), selectedLanguage));
				}
			}));
		}
	}

	protected void registerLoginHandlers(final AsyncCallback<?> callback) {
		LoginHandler loginHandler = new LoginHandler() {

			@Override
			public void onSubmit(LoginEvent loginEvent) {
				doLogin(constructLoginToken(loginEvent), callback);
			}
		};

		registerHandler(addLoginHandler(loginHandler));
	}

	@SuppressWarnings("unchecked")
	protected void doLogin(LoginToken token, AsyncCallback<?> callback) {
		if (broadcaster != null) {
			if (redirectUrl != null && !redirectUrl.isEmpty()) {
				broadcaster.authenticate(token, (AsyncCallback<String>) callback);
			} else {
				broadcaster.login(token, (AsyncCallback<ClientSession>) callback);
			}
		} else {
			callback.onFailure(new BroadcastingException("Broadcaster is null."));
		}
	}

	protected void handleFailedLogin() {
		clearLoginCookies();
		display.onLoginFailed();
		display.showMessage(loginMessages.loginFailedTitle());
	}

	protected void clearLoginCookies() {
		Cookies.removeCookie(LoginConstants.LANGUAGE_COOKIE_NAME);
		Cookies.removeCookie(LoginConstants.LOGINNAME_COOKIE_NAME);
		Cookies.removeCookie(LoginConstants.LOGINPASSWORD_COOKIE_NAME);
	}

	protected void handleSuccessfulLogin(ClientSession result) {
		display.setUsername("");
		display.setPassword("");

		unbind();

		if (redirectUrl != null && !redirectUrl.isEmpty()) {
			String sessionId = result.getSessionId();
			String query = LoginUtils.getCommonQueryString(sessionId);
			RootPanel.get().clear();
			Location.replace(redirectUrl + query);
		}
	}
}
