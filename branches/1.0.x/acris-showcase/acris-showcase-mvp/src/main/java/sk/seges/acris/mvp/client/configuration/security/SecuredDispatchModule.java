package sk.seges.acris.mvp.client.configuration.security;

import sk.seges.acris.security.shared.session.ClientSession;

import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.philbeaudoin.gwtp.dispatch.client.DefaultExceptionHandler;
import com.philbeaudoin.gwtp.dispatch.client.DispatchAsync;
import com.philbeaudoin.gwtp.dispatch.client.ExceptionHandler;
import com.philbeaudoin.gwtp.dispatch.client.SecurityCookieAccessor;
import com.philbeaudoin.gwtp.dispatch.client.SecurityCookieAccessorImpl;
import com.philbeaudoin.gwtp.dispatch.client.gin.AbstractDispatchModule;

public class SecuredDispatchModule extends AbstractDispatchModule {

	/**
	 * Constructs a new GIN configuration module that sets up a secure
	 * {@link com.philbeaudoin.gwtp.dispatch.client.DispatchAsync} implementation, using the
	 * {@link com.philbeaudoin.gwtp.dispatch.client.DefaultExceptionHandler} and the
	 * {@link com.philbeaudoin.gwtp.dispatch.client.SecurityCookieAccessorImpl}.
	 */
	public SecuredDispatchModule() {
		this(DefaultExceptionHandler.class, SecurityCookieAccessorImpl.class);
	}

	@Override
	protected void configure() {
		super.configure();
		bind(ClientSession.class).asEagerSingleton();
	}

	/**
	 * Constructs a new GIN configuration module that sets up a secure
	 * {@link com.philbeaudoin.gwtp.dispatch.client.DispatchAsync} implementation, using the provided
	 * {@link ExceptionHandler} implementation class.
	 * 
	 * @param exceptionHandlerType The {@link ExceptionHandler} implementation class.
	 * @param SecurityCookieAccessorType The {@link SecurityCookieAccessor} implementation class.
	 */
	public SecuredDispatchModule(Class<? extends ExceptionHandler> exceptionHandlerType, Class<? extends SecurityCookieAccessor> SecurityCookieAccessorType) {
		super(exceptionHandlerType, SecurityCookieAccessorType);
	}

	@Provides
	@Singleton
	protected DispatchAsync provideDispatchAsync(ClientSession clientSession, ExceptionHandler exceptionHandler, SecurityCookieAccessor secureSessionAccessor) {
		return new SecuredDispatchAsync(clientSession, exceptionHandler, secureSessionAccessor);
	}

}
