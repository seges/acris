package sk.seges.acris.showcase.deployer.server.appengine;

import sk.seges.acris.security.server.service.GuiceRemoteServiceServlet;
import sk.seges.acris.security.server.service.OpenIDConsumerService;
import sk.seges.acris.security.shared.service.IOpenIDConsumerService;

import com.google.inject.Scopes;
import com.google.inject.servlet.ServletModule;

public class AppEngineGuiceServletModule extends ServletModule {

	@Override
	protected void configureServlets() {
		serve("/sk.seges.acris.demo.OpenID/GWT.rpc").with(GuiceRemoteServiceServlet.class);

		bind(IOpenIDConsumerService.class).to(OpenIDConsumerService.class).in(Scopes.SINGLETON);
	}
}
