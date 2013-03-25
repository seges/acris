package sk.seges.acris.security.server.spring.user_management.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.security.userdetails.UsernameNotFoundException;

public interface WebIdUserDetailsService extends UserDetailsService {

	UserDetails loadUserByUsernameAndWebId(String username, String webId) throws UsernameNotFoundException, DataAccessException;
}
