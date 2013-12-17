package sk.seges.acris.security.server.spring.user_management.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import sk.seges.acris.security.server.spring.user_management.dao.user.api.IGenericUserDao;
import sk.seges.acris.security.shared.spring.user_management.domain.SpringUserAdapter;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

public class SpringUserService implements WebIdUserDetailsService {

	protected IGenericUserDao<UserData> genericUserDao;

	public SpringUserService(IGenericUserDao<UserData> genericUserDao) {
		this.genericUserDao = genericUserDao;
	}

	@Transactional(propagation = Propagation.REQUIRED)
	public UserDetails loadUserByUsernameAndWebId(String userName, String webId) throws UsernameNotFoundException, DataAccessException {
		UserData userData = genericUserDao.findUser(userName, webId);
		if (userData instanceof UserDetails) {
			return (UserDetails) userData;
		}
		return new SpringUserAdapter(userData);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException, DataAccessException {
		UserData userData = genericUserDao.findByUsername(username);
		if (userData instanceof UserDetails) {
			return (UserDetails) userData;
		}
		return new SpringUserAdapter(userData);
	}
}