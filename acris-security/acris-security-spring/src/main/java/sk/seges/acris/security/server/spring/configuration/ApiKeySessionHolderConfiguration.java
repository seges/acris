package sk.seges.acris.security.server.spring.configuration;

import org.springframework.context.annotation.Bean;

import sk.seges.acris.security.shared.apikey.ApiKeySessionHolder;

public class ApiKeySessionHolderConfiguration {

	@Bean
	public ApiKeySessionHolder apiKeySessionHolder() {
		return new ApiKeySessionHolder();
	}
}
