package sk.seges.acris.mvp.client.configuration.security;

import sk.seges.acris.security.shared.session.ClientSession;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.gwtplatform.dispatch.client.DispatchAsync;
import com.gwtplatform.dispatch.client.ExceptionHandler;
import com.gwtplatform.dispatch.client.SecurityCookieAccessor;
import com.gwtplatform.dispatch.client.actionhandler.ClientActionHandlerRegistry;
import com.gwtplatform.dispatch.client.gin.DispatchAsyncModule;

public class SecuredDispatchModule extends DispatchAsyncModule {

	@Override
	protected void configure() {
		super.configure();
		bind(ClientSession.class).asEagerSingleton();
	}

	@Provides
	@Singleton
	protected DispatchAsync provideDispatchAsync(ClientSession clientSession, ExceptionHandler exceptionHandler,
			SecurityCookieAccessor secureSessionAccessor, ClientActionHandlerRegistry registry) {
		return new SecuredDispatchAsync(clientSession, exceptionHandler, secureSessionAccessor, registry);
	}

}
