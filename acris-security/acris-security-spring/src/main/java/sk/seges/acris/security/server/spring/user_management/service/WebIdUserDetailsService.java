package sk.seges.acris.security.server.spring.user_management.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface WebIdUserDetailsService extends UserDetailsService {

	UserDetails loadUserByUsernameAndWebId(String username, String webId) throws UsernameNotFoundException, DataAccessException;
}
