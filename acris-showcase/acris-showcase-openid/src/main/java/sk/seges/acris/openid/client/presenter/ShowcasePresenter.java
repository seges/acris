package sk.seges.acris.openid.client.presenter;

import sk.seges.acris.common.util.Pair;
import sk.seges.acris.security.client.presenter.OpenIDLoginPresenter;
import sk.seges.acris.security.shared.service.IOpenIDConsumerServiceAsync;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.service.UserServiceBroadcaster;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;

public class ShowcasePresenter extends OpenIDLoginPresenter {

	public ShowcasePresenter(OpenIDLoginDisplay display, UserServiceBroadcaster broadcaster, String redirectUrl,
			Pair<String, String>[] enabledLanguages, boolean rememberMeEnabled,
			IOpenIDConsumerServiceAsync consumerService) {
		super(display, broadcaster, redirectUrl, enabledLanguages, rememberMeEnabled, consumerService);
	}

	@Override
	protected void handleSuccessfulLogin(ClientSession result) {
		super.handleSuccessfulLogin(result);
		
		if (redirectUrl == null) {
			RootPanel.get().clear();
			RootPanel.get().add(new Label("Login successful!"));
		}
	}
}
