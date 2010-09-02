package sk.seges.acris.mvp.server.initialization;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.user_management.dao.user.IGenericUserDao;
import sk.seges.acris.security.shared.user_management.domain.api.UserData;
import sk.seges.acris.security.shared.user_management.domain.twig.TwigGenericUser;

@Component
public class UserInitializer implements IUserInitializer {
	
	@Autowired
	private IGenericUserDao<UserData> userDao;
	
	@Transactional(propagation=Propagation.REQUIRES_NEW)
	public void initialize() {
		TwigGenericUser user = new TwigGenericUser();
		user.setUsername("admin");
		user.setPassword("admin");
		user.setDescription("administrator");
		userDao.persist(user);
	}
}