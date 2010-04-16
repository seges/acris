package sk.seges.acris.security.server.user_management.service.user;

import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import sk.seges.acris.security.server.user_management.dao.user.IGenericUserDao;

public class HibernateUserService implements UserDetailsService {

	private static final long serialVersionUID = 8634166450216274971L;

	protected IGenericUserDao genericUserDao;
			
	public void setGenericUserDao(IGenericUserDao genericUserDao) {
		this.genericUserDao = genericUserDao;
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public UserDetails loadUserByUsername(String userName)
			throws UsernameNotFoundException, DataAccessException {
		return genericUserDao.findByUsername(userName);
	}
}