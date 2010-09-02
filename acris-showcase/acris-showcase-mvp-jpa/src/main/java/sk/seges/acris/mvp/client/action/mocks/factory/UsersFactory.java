package sk.seges.acris.mvp.client.action.mocks.factory;

import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;


public class UsersFactory {
	
	public static final UserData createMockUser(Long id, String name, String password) {
		GenericUserDTO user = new GenericUserDTO();
		user.setDescription("Mock user");
		user.setPassword(password);
		user.setUsername(name);
		user.setId(id);
		user.setEnabled(true);
		
		return user;
	}
}
