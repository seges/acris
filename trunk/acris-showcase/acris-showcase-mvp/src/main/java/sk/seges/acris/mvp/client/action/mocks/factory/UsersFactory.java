package sk.seges.acris.mvp.client.action.mocks.factory;

import sk.seges.acris.security.rpc.user_management.domain.GenericUser;


public class UsersFactory {
	
	public static final GenericUser createMockUser(Long id, String name, String password) {
		GenericUser user = new GenericUser();
		user.setDescription("Mock user");
		user.setPassword(password);
		user.setUsername(name);
		user.setId(id);
		user.setEnabled(true);
		
		return user;
	}
}
