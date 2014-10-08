package sk.seges.acris.generator.server.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import sk.seges.acris.generator.server.rewriterules.INiceUrlGenerator;
import sk.seges.acris.generator.server.rewriterules.SunConditionalNiceURLGenerator;

public class RedirectConfiguration {

	@Autowired
	@Qualifier("url.redirect.file.location")
	private String redirectFilePath;

	@Autowired
	@Qualifier("legacy.url.redirect.single.file")
	private Boolean legacyRedirectSingleFile;

	@Autowired
	@Qualifier("legacy.url.redirect.file.location")
	private String legacyRedirectFilePath;

	@Autowired
	@Qualifier("url.redirect.single.file")
	private Boolean redirectSingleFile;

	@Autowired
	@Qualifier("url.redirect.condition")
	private Boolean redirectCondition;

	@Bean
	@Scope("prototype")
	public INiceUrlGenerator sunConditionalURLGenerator() {
		return new SunConditionalNiceURLGenerator(redirectFilePath, redirectCondition, redirectSingleFile, legacyRedirectFilePath, legacyRedirectSingleFile);
	}
}
