package sk.seges.acris.security.shared.service;

import java.util.Map;

import sk.seges.acris.security.shared.dto.OpenIDUserDTO;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * OpenID authentication and verification service.
 */
@RemoteServiceRelativePath("openId")
public interface IOpenIDConsumerRemoteService extends RemoteService {

	OpenIDUserDTO authenticate(String userSuppliedString, String returnToUrl, String realm);

	OpenIDUserDTO verify(String queryString, Map<String, String[]> parameterMap);
}
