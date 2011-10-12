package sk.seges.acris.security.configuration;

import org.springframework.context.annotation.Bean;

import sk.seges.acris.security.rest.RestAuthenticationResource;

public class RestAuthenticationConfiguration {

	@Bean
	public RestAuthenticationResource restAuthenticationResource() {
		return new RestAuthenticationResource();
	}
}
