package sk.seges.acris.openid.server.service;

import sk.seges.acris.security.server.service.GuiceRemoteServiceServlet;
import sk.seges.acris.security.server.service.OpenIDConsumerService;
import sk.seges.acris.security.shared.service.IOpenIDConsumerService;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;

public class ShowcaseGuiceServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		serve("/sk.seges.acris.openid.OpenIDShowcase/GWT.rpc").with(GuiceRemoteServiceServlet.class);

		bind(IOpenIDConsumerService.class).to(OpenIDConsumerService.class).in(Scopes.SINGLETON);
	}
}
