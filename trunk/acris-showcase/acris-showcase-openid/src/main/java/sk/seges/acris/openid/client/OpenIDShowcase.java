package sk.seges.acris.openid.client;

import sk.seges.acris.common.util.Pair;
import sk.seges.acris.openid.client.presenter.ShowcasePresenter;
import sk.seges.acris.openid.client.service.MockUserService;
import sk.seges.acris.openid.client.view.ShowcaseView;
import sk.seges.acris.security.client.presenter.OpenIDLoginPresenter;
import sk.seges.acris.security.client.presenter.OpenIDLoginPresenter.OpenIDLoginDisplay;
import sk.seges.acris.security.shared.service.IOpenIDConsumerService;
import sk.seges.acris.security.shared.service.IOpenIDConsumerServiceAsync;
import sk.seges.acris.security.shared.user_management.service.UserServiceBroadcaster;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.SimplePanel;

/**
 * Simple entry point with OpenID login dialog which sends session id to desired
 * url on successful login.
 * 
 * @author Martin
 */
public class OpenIDShowcase implements EntryPoint {

	@SuppressWarnings("unchecked")
	@Override
	public void onModuleLoad() {
		UserServiceBroadcaster broadcaster = new UserServiceBroadcaster();
		broadcaster.addUserService(new MockUserService());

		IOpenIDConsumerServiceAsync consumerService = GWT.create(IOpenIDConsumerService.class);
		((ServiceDefTarget) consumerService).setServiceEntryPoint("GWT.rpc");

		Pair<String, String>[] languages = new Pair[2];
		languages[0] = new Pair<String, String>("sk", "slovensky");
		languages[1] = new Pair<String, String>("en", "english");

		boolean rememberMeAware = true;

		String redirectUrl = "/sk.seges.acris.demo.Reporting/Reporting.html"; //GWT.getModuleBaseURL() + "Redirect.html";

		OpenIDLoginDisplay display = GWT.create(ShowcaseView.class);

		OpenIDLoginPresenter presenter = new ShowcasePresenter(display, broadcaster, redirectUrl, languages,
				rememberMeAware, consumerService);

		SimplePanel overlay = new SimplePanel();
		overlay.addStyleName("acris-login-overlay");
		RootPanel.get().add(overlay);

		presenter.bind(RootPanel.get());
	}
}
