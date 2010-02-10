package sk.seges.acris.security.client.proxy;

import sk.seges.acris.callbacks.client.TrackingAsyncCallback;
import sk.seges.acris.security.rpc.to.ClientContextHolder;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.Serializer;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;

public class SessionAwareRemoteServiceProxy extends RemoteServiceProxy {

	private final ClientContextHolder sessionProvider;

	protected SessionAwareRemoteServiceProxy(String moduleBaseURL,
			String remoteServiceRelativePath, String serializationPolicyName,
			Serializer serializer) {
		super(moduleBaseURL, remoteServiceRelativePath,
				serializationPolicyName, serializer);

		sessionProvider = GWT.create(ClientContextHolder.class);

	}

	private static long uniqueRequestId = 0;
	
	@Override
	protected <T> Request doInvoke(ResponseReader responseReader,
			String methodName, int invocationCount, String requestData,
			AsyncCallback<T> callback) {

		long lastUniqueRequestID = uniqueRequestId;
		uniqueRequestId++;
		
		if (callback instanceof TrackingAsyncCallback<?>) {
			((TrackingAsyncCallback<T>) callback).setRequestId(lastUniqueRequestID);
		}

		String sessionID = "";

		if (sessionProvider.getClientContext() != null) {
			sessionID = sessionProvider.getClientContext().getSessionId();
		}

		if (sessionID == null) {
			sessionID = "";
		}
		
		if (sessionID.length() > 0) {
			final char sep = '\uffff';
			return super.doInvoke(responseReader, methodName, invocationCount,
					String.valueOf(sep) + sessionID + String.valueOf(sep) + requestData, callback);
		}

		return super.doInvoke(responseReader, methodName, invocationCount, requestData, callback);
	}
}