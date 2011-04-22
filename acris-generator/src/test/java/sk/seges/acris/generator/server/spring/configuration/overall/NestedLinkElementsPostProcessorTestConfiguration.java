package sk.seges.acris.generator.server.spring.configuration.overall;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.NiceURLLinkPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.OfflineSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;

@Import({/*PostProcessorsConfiguration.class, */MockTestConfiguration.class, OfflineSettingsConfiguration.class, WebSettingsServiceConfiguration.class})
public class NestedLinkElementsPostProcessorTestConfiguration {

	@Bean
	public AbstractElementPostProcessor niceURLLinkPostProcessor() {
		return new NiceURLLinkPostProcessor();
	}

//	@Bean
//	@Qualifier("legacy.url.redirect.single.file")
//	Boolean legacyUrlRedirectSingleFile() {
//		return true;
//	}
//
//	@Bean
//	@Qualifier("legacy.url.redirect.file.location")
//	String legacyUrlRedirectFileLocation() {
//		return "";
//	}
//	
//	@Bean
//	@Qualifier("url.redirect.file.location")
//	String urlRedirectFileLocation() {
//		return "";
//	}
//	
//	@Bean
//	@Qualifier("url.redirect.condition")
//	Boolean urlRedirectCondition() {
//		return true;
//	}
//	
//	@Bean
//	@Qualifier("url.redirect.single.file")
//	Boolean urlRedirectSingleFile() {
//		return true;
//	}

}