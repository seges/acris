package sk.seges.acris.mvp.client.configuration.security;

import sk.seges.acris.security.client.session.SessionServiceDefTarget;
import sk.seges.acris.security.shared.session.ClientSession;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.ServiceDefTarget;
import com.philbeaudoin.gwtp.dispatch.client.DispatchAsync;
import com.philbeaudoin.gwtp.dispatch.client.DispatchService;
import com.philbeaudoin.gwtp.dispatch.client.DispatchServiceAsync;
import com.philbeaudoin.gwtp.dispatch.client.ExceptionHandler;
import com.philbeaudoin.gwtp.dispatch.client.SecurityCookieAccessor;
import com.philbeaudoin.gwtp.dispatch.shared.Action;
import com.philbeaudoin.gwtp.dispatch.shared.Result;

public class SecuredDispatchAsync implements DispatchAsync {

	private static final DispatchServiceAsync realService = GWT.create(DispatchService.class);
	private final ExceptionHandler exceptionHandler;
	private final SecurityCookieAccessor securityCookieAccessor;
	private final String baseUrl;

	public SecuredDispatchAsync(ClientSession clientSession, ExceptionHandler exceptionHandler, SecurityCookieAccessor securityCookieAccessor) {
		this.exceptionHandler = exceptionHandler;
		this.securityCookieAccessor = securityCookieAccessor;

		SessionServiceDefTarget endpoint = (SessionServiceDefTarget) realService;
		endpoint.setSession(clientSession);

		String entryPointUrl = ((ServiceDefTarget) realService).getServiceEntryPoint();

		if (entryPointUrl == null) this.baseUrl = "";
		else this.baseUrl = entryPointUrl;
	}

	public <A extends Action<R>, R extends Result> void execute(final A action, final AsyncCallback<R> callback) {
		((ServiceDefTarget) realService).setServiceEntryPoint(baseUrl + action.getServiceName());

		String securityCookie = securityCookieAccessor.getCookieContent();

		realService.execute(securityCookie, action, new AsyncCallback<Result>() {

			public void onFailure(Throwable caught) {
				SecuredDispatchAsync.this.onFailure(action, caught, callback);
			}

			@SuppressWarnings("unchecked")
			public void onSuccess(Result result) {
				// Note: This cast is a dodgy hack to get around a GWT 1.6 async
				// compiler issue
				SecuredDispatchAsync.this.onSuccess(action, (R) result, callback);
			}
		});
	}

	protected <A extends Action<R>, R extends Result> void onSuccess(A action, R result, final AsyncCallback<R> callback) {
		callback.onSuccess(result);
	}

	protected <A extends Action<R>, R extends Result> void onFailure(A action, Throwable caught, final AsyncCallback<R> callback) {
		if (exceptionHandler != null && exceptionHandler.onFailure(caught) == ExceptionHandler.Status.STOP) {
			return;
		}

		callback.onFailure(caught);
	}

}
