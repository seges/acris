package sk.seges.acris.security.client.presenter;

import sk.seges.acris.security.client.handler.HasLoginHandlers;
import sk.seges.acris.security.client.handler.LoginHandler;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.api.LoginToken;
import sk.seges.acris.security.shared.user_management.service.UserServiceBroadcaster;
import sk.seges.acris.util.Pair;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Widget;

public class LoginPresenter {

	public interface LoginDisplay extends HasLoginHandlers {

		Widget asWidget();

		void onLoginFailed();

		void setEnabledLanguages(Pair<String, String>[] languages);

		void setSelectedLanguage(String selectedLanguage);

		void setRememberMeMode(boolean rememberMeAware);
		
		void displayMessage(String message);

		void showMessage(String message);
	}

	protected LoginDisplay display;
	protected UserServiceBroadcaster broadcaster;

	public LoginPresenter(LoginDisplay display, UserServiceBroadcaster broadcaster) {
		this.display = display;
		this.broadcaster = broadcaster;
	}

	public void doLogin(LoginToken token, AsyncCallback<ClientSession> callback) {
		broadcaster.login(token, callback);
	}

	public void doRedirect(String redirectUrl) {
		Window.Location.replace(redirectUrl);
	}

	public void initDisplay(Pair<String, String>[] languages, boolean rememberMeAware, LoginHandler loginHandler) {
		display.setEnabledLanguages(languages);
		display.setRememberMeMode(rememberMeAware);
		display.addLoginHandler(loginHandler);
	}

	public void onLoginFailed() {
		display.onLoginFailed();
	}

	public LoginDisplay getDisplay() {
		return display;
	}
}
