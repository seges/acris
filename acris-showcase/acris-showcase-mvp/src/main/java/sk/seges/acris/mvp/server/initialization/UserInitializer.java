package sk.seges.acris.mvp.server.initialization;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import sk.seges.acris.mvp.server.dao.IUserDao;
import sk.seges.acris.security.rpc.user_management.domain.GenericUser;

@Component
public class UserInitializer {
	
	@Autowired
	private IUserDao userDao;
	
	@PostConstruct
	public void initialize() {
		GenericUser genericUser = new GenericUser();
		userDao.persist(genericUser);
	}
}