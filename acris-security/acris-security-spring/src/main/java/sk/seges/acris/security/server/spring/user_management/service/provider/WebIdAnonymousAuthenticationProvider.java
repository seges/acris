package sk.seges.acris.security.server.spring.user_management.service.provider;

import org.springframework.security.authentication.AnonymousAuthenticationProvider;

public class WebIdAnonymousAuthenticationProvider extends AnonymousAuthenticationProvider {

	public WebIdAnonymousAuthenticationProvider(String key) {
		super(key);
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return  (WebIdAnonymousAuthenticationToken.class.isAssignableFrom(authentication));
	}
}
