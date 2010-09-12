package sk.seges.acris.mvp.server.initialization;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.GrantedAuthority;
import org.springframework.security.GrantedAuthorityImpl;
import org.springframework.security.context.SecurityContextHolder;
import org.springframework.security.providers.anonymous.AnonymousAuthenticationToken;
import org.springframework.stereotype.Component;

import sk.seges.acris.security.shared.user_management.domain.dto.GenericUserDTO;

@Component
public class DBInitializer {

	@Autowired
	private IUserInitializer userInitializer;

	@PostConstruct
	public void initialize() {
		SecurityContextHolder.getContext().setAuthentication(
				new AnonymousAuthenticationToken("initialization", new GenericUserDTO(), new GrantedAuthority[] {new GrantedAuthorityImpl("dump")}));
		userInitializer.initialize();
	}
}