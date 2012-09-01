package sk.seges.acris.security.client.session;

import sk.seges.acris.callbacks.client.TrackingAsyncCallback;
import sk.seges.acris.security.shared.session.ClientSession;

import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;
import com.google.gwt.user.client.rpc.impl.Serializer;

/**
 * {@link RemoteServiceProxy} extension for send current session id in request
 * payload. SessionId is obtained from {@link ClientSession} object which has to
 * be correctly initialized and assigned with the service.
 * 
 * @author fat
 */
public abstract class SessionEnabledRemoteServiceProxy extends RemoteServiceProxy
		implements SessionServiceDefTarget {

	/**
	 * ClientSession for holding surrent sessionId. This sessionId will be part
	 * of request's payload.
	 */
	private ClientSession clientSession;

	private static long uniqueRequestId = 0;

	protected SessionEnabledRemoteServiceProxy(String moduleBaseURL,
			String remoteServiceRelativePath, String serializationPolicyName,
			Serializer serializer) {
		super(moduleBaseURL, remoteServiceRelativePath,
				serializationPolicyName, serializer);
	}

	public ClientSession getSession() {
		return clientSession;
	}

	public void setSession(ClientSession clientSession) {
		this.clientSession = clientSession;
	}

	  protected <T> Request doInvoke(ResponseReader responseReader,
		      String methodName, RpcStatsContext statsContext, String requestData,
		      AsyncCallback<T> callback) {

		long lastUniqueRequestID = uniqueRequestId;
		uniqueRequestId++;

		if (callback instanceof TrackingAsyncCallback<?>) {
			((TrackingAsyncCallback<T>) callback)
					.setRequestId(lastUniqueRequestID);
		}

		String sessionID = "";

		if (getSession() != null) {
			sessionID = getSession().getSessionId();
		}

		if (sessionID == null) {
			sessionID = "";
		}

		if (sessionID.length() > 0) {
			final char sep = '\uffff';
			return super.doInvoke(responseReader, methodName, statsContext,
					String.valueOf(sep) + sessionID + String.valueOf(sep)
							+ requestData, callback);
		}

		return super.doInvoke(responseReader, methodName, statsContext,
				requestData, callback);
	}
}