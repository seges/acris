package sk.seges.acris.asmant.server.configuration;

import org.springframework.context.annotation.Bean;

import sk.seges.acris.asmant.server.rest.SiteResource;


/**
 * @author ladislav.gazo
 */
public class AsmantConfiguration {
	@Bean
	public SiteResource siteResource() {
		return new SiteResource();
	}
}
