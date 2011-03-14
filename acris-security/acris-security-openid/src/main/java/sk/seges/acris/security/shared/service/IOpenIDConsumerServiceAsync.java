package sk.seges.acris.security.shared.service;

import java.util.Map;

import sk.seges.acris.security.shared.dto.OpenIDUserDTO;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * OpenID authentication and verification service.
 */
public interface IOpenIDConsumerServiceAsync {

	void authenticate(String userSuppliedString, String returnToUrl, String realm, AsyncCallback<OpenIDUserDTO> callback);

	void verify(String queryString, Map<String, String[]> parameterMap, AsyncCallback<OpenIDUserDTO> callback);
}
