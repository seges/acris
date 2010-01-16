package sk.seges.acris.security.server.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.AccessDeniedException;
import org.springframework.security.Authentication;
import org.springframework.security.AuthenticationManager;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.context.SecurityContextImpl;
import org.springframework.security.providers.UsernamePasswordAuthenticationToken;
import org.springframework.security.userdetails.UserDetails;
import org.springframework.security.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Qualifier("loginUserService")
public class LoginUserService {

	private AuthenticationManager authenticationManager;

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Transactional(propagation=Propagation.REQUIRED)
	protected UserDetails loginUser(String username, String password, String language) {
		
		UserDetails user = userDetailsService.loadUserByUsername(username);
		
		if (user == null) {
			throw new AccessDeniedException("User does not exists");
		}
		
		Authentication auth = authenticationManager
				.authenticate(new UsernamePasswordAuthenticationToken(user,
						password, user.getAuthorities()));
		SecurityContext sc = new SecurityContextImpl();
		sc.setAuthentication(auth);

		SecurityContextHolder.setContext(sc);
		
		return user;
	}
	
	 public void logout() {
	     SecurityContextHolder.clearContext();
	 }
	 
	 public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
}