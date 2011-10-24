package sk.seges.acris.test.shared.service;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface CardPayRemoteServiceAsync {

	void computeSign(String vs, String res, String ac, AsyncCallback<String> callback);
}
