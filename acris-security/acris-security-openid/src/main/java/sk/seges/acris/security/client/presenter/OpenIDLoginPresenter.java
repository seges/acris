package sk.seges.acris.security.client.presenter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import sk.seges.acris.core.client.util.JavaScriptUtils;
import sk.seges.acris.security.client.handler.HasOpenIDLoginHandlers;
import sk.seges.acris.security.client.handler.LoginHandler;
import sk.seges.acris.security.client.handler.OpenIDLoginHandler;
import sk.seges.acris.security.shared.IOpenIDConsumerServiceAsync;
import sk.seges.acris.security.shared.data.OpenIDUser;
import sk.seges.acris.security.shared.session.ClientSession;
import sk.seges.acris.security.shared.user_management.domain.OpenIDLoginToken;
import sk.seges.acris.security.shared.user_management.service.UserServiceBroadcaster;
import sk.seges.acris.util.Pair;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;

public class OpenIDLoginPresenter extends LoginPresenter {

	public interface OpenIDLoginDisplay extends LoginDisplay, HasOpenIDLoginHandlers {
	}

	protected IOpenIDConsumerServiceAsync consumerService;

	public OpenIDLoginPresenter(OpenIDLoginDisplay display, UserServiceBroadcaster broadcaster,
			IOpenIDConsumerServiceAsync consumerService) {
		super(display, broadcaster);
		this.consumerService = consumerService;
	}

	public void authenticate(String identifier) {
		consumerService.authenticate(identifier, getModuleUrl(), new AsyncCallback<OpenIDUser>() {

			@Override
			public void onSuccess(OpenIDUser result) {
				if (result != null) {
					String url = result.getRedirectUrl();
					url += "&openid.ns.ui="
							+ JavaScriptUtils.encodeURIComponent("http://specs.openid.net/extensions/ui/1.0");
					url += "&openid.ui.mode=" + JavaScriptUtils.encodeURIComponent("popup");
					Window.open(url, "openIDPopup", "width = 500," + "height = 540," + "left = 200," + "top = 200,"
							+ "resizable = no," + "scrollbars = no," + "status = no," + "toolbar = no");
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

	public void verify(final AsyncCallback<ClientSession> callback) {
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

				consumerService.verify(href, map, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
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
	public void doRedirect(String redirectUrl) {
		closePopup(redirectUrl);
		super.doRedirect(redirectUrl);
	}

	public void initDisplay(Pair<String, String>[] languages, boolean rememberMeAware, LoginHandler loginHandler,
			OpenIDLoginHandler openIDLoginHandler) {
		super.initDisplay(languages, rememberMeAware, loginHandler);
		((OpenIDLoginDisplay) display).addOpenIDLoginHandler(openIDLoginHandler);
	};

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
