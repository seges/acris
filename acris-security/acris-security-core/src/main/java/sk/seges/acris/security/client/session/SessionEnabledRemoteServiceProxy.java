package sk.seges.acris.security.client.session;

import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RpcRequestBuilder;
import com.google.gwt.user.client.rpc.impl.RemoteServiceProxy;
import com.google.gwt.user.client.rpc.impl.RequestCallbackAdapter.ResponseReader;
import com.google.gwt.user.client.rpc.impl.RpcStatsContext;
import com.google.gwt.user.client.rpc.impl.Serializer;
import sk.seges.acris.callbacks.client.TrackingAsyncCallback;
import sk.seges.acris.security.shared.session.ClientSession;

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

	protected final RpcRequestBuilder postBuilder;
	protected final RpcRequestBuilder getBuilder;

	interface DelimiterProvider {
		String getSessionDelimiter();
	}

	private static final String POST_SESSION_DELIMITER = String.valueOf('\uffff');
	private static final String GET_SESSION_DELIMITER = "%EF%BF%BD";

	abstract class CustomRpcRequestBuilder extends RpcRequestBuilder implements DelimiterProvider {}

	protected SessionEnabledRemoteServiceProxy(String moduleBaseURL,
			String remoteServiceRelativePath, String serializationPolicyName,
			Serializer serializer) {
		super(moduleBaseURL, remoteServiceRelativePath,
				serializationPolicyName, serializer);

		getBuilder = new CustomRpcRequestBuilder() {

			private RequestCallback callback;
			private String contentType;
			private String data;
			private int id;

			protected RequestBuilder doCreate(String serviceEntryPoint) {
				return new RequestBuilder(RequestBuilder.GET, serviceEntryPoint);
			}

			@Override
			protected void doSetCallback(RequestBuilder rb, RequestCallback callback) {
				this.callback = callback;
				super.doSetCallback(rb, callback);
			}

			@Override
			protected void doSetContentType(RequestBuilder rb, String contentType) {
				this.contentType = contentType;
				super.doSetContentType(rb, contentType);
			}

			@Override
			protected void doSetRequestData(RequestBuilder rb, String data) {
				this.data = data;
				super.doSetRequestData(rb, data);
			}

			@Override
			protected void doSetRequestId(RequestBuilder rb, int id) {
				this.id = id;
				super.doSetRequestId(rb, id);
			}

			@Override
			protected void doFinish(RequestBuilder rb) {
				create(getServiceEntryPoint() + "?" + data.replaceAll(POST_SESSION_DELIMITER, GET_SESSION_DELIMITER));
				super.setCallback(callback);
				super.setContentType(contentType);
				super.setRequestId(id);
				super.doFinish(rb);
			}

			@Override
			public String getSessionDelimiter() {
				return GET_SESSION_DELIMITER;
			}
		};

		postBuilder = new CustomRpcRequestBuilder() {

			@Override
			public String getSessionDelimiter() {
				return POST_SESSION_DELIMITER;
			}
		};
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
			((TrackingAsyncCallback<T>) callback).setName(getClass().getName() + "." + methodName);
		}

		String sessionID = "";

		if (getSession() != null) {
			sessionID = getSession().getSessionId();
		}

		if (sessionID == null) {
			sessionID = "";
		}

		if (sessionID.length() > 0) {

			return super.doInvoke(responseReader, methodName, statsContext,
					POST_SESSION_DELIMITER + sessionID + POST_SESSION_DELIMITER + requestData, callback);
		}

		return super.doInvoke(responseReader, methodName, statsContext,
				requestData, callback);
	}
}