package sk.seges.acris.security.client.openid.configuration;

import com.google.gwt.user.client.Window;

public class DefaultOpenIdConfiguration implements OpenIdConfiguration {

	@Override
	public String getRealm() {
		return Window.Location.getProtocol() + "//" + Window.Location.getHost();
	}

}