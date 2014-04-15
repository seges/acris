package sk.seges.acris.security.server.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.security.access.intercept.RunAsImplAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import sk.seges.acris.security.server.spring.user_management.dao.user.api.IGenericUserDao;
import sk.seges.acris.security.server.spring.user_management.service.SpringUserService;
import sk.seges.acris.security.server.spring.user_management.service.WebIdUserDetailsService;
import sk.seges.acris.security.server.spring.user_management.service.provider.WebIdAnonymousAuthenticationProvider;
import sk.seges.acris.security.server.spring.user_management.service.provider.WebIdDaoAuthenticationProvider;
import sk.seges.corpis.server.domain.user.server.model.data.UserData;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationManagerConfiguration {

	@Bean
	public WebIdUserDetailsService userDetailsService(IGenericUserDao<UserData> genericUserDao) {
		return new SpringUserService(genericUserDao);
	}

	@Bean
	public WebIdDaoAuthenticationProvider daoAuthenticationProvider(WebIdUserDetailsService userDetailsService) {
		WebIdDaoAuthenticationProvider webIdDaoAuthenticationProvider = new WebIdDaoAuthenticationProvider();
		webIdDaoAuthenticationProvider.setUserDetailsService(userDetailsService);
		return webIdDaoAuthenticationProvider;
	}

	@Bean
	public RunAsImplAuthenticationProvider runAsAuthenticationProvider() {
		RunAsImplAuthenticationProvider runAsImplAuthenticationProvider = new RunAsImplAuthenticationProvider();
		//TODO not so smart!!
		runAsImplAuthenticationProvider.setKey("secretRunAsKey");
		return runAsImplAuthenticationProvider;
	}
}
