package sk.seges.acris.openid.client.configuration;

import sk.seges.acris.security.shared.configuration.LoginConfiguration;
import sk.seges.acris.security.shared.util.LoginUtils;

public class ShowcaseLoginConfiguration implements LoginConfiguration {

	@Override
	public String getAdminUrl() {
		String baseUrl = LoginUtils.getModuleBaseUrl();
		return baseUrl = baseUrl + "sk.seges.acris.openid.OpenIDShowcase/RedirectIDShowcase.html";
	}

	@Override
	public String getLoginModule() {
		String baseUrl = LoginUtils.getModuleBaseUrl();
		return baseUrl + "sk.seges.acris.openid.OpenIDShowcase";
	}

	@Override
	public String getLoginUrl() {
		return getLoginModule() + "/OpenIDShowcase.html";
	}
}
