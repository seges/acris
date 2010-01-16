package sk.seges.acris.security.rpc.service;

import sk.seges.acris.security.rpc.domain.GenericUser;
import sk.seges.acris.security.rpc.to.ClientContext;

import com.google.gwt.user.client.rpc.RemoteService;

public interface IUserService extends RemoteService {
	
//	ClientContext login(String username, String password, String language, IUserDetail userDetail);
	
	ClientContext login(String username, String password, String language);

	GenericUser getLoggedUser();

	void logout();
	
//	List<String> getAuditTrailedLoggedUsernames();
}
