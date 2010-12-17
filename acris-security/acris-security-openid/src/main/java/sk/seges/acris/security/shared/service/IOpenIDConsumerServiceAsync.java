package sk.seges.acris.security.shared.service;

import java.util.Map;

import sk.seges.acris.security.shared.data.OpenIDUser;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * OpenID authentication and verification service.
 */
public interface IOpenIDConsumerServiceAsync {

	void authenticate(String userSuppliedString, String returnToUrl, AsyncCallback<OpenIDUser> callback);

	void verify(String queryString, Map<String, String[]> parameterMap, AsyncCallback<String> callback);
}
