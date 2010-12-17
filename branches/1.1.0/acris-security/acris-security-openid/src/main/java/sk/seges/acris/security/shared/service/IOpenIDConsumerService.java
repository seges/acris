package sk.seges.acris.security.shared.service;

import java.util.Map;

import sk.seges.acris.security.shared.data.OpenIDUser;

import com.google.gwt.user.client.rpc.RemoteService;

/**
 * OpenID authentication and verification service.
 */
public interface IOpenIDConsumerService extends RemoteService {

	OpenIDUser authenticate(String userSuppliedString, String returnToUrl);

	String verify(String queryString, Map<String, String[]> parameterMap);
}
