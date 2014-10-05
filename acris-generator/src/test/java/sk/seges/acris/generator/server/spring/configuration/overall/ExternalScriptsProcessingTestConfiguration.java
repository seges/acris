package sk.seges.acris.generator.server.spring.configuration.overall;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.AcrisExternalScriptAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.AcrisExternalScriptAppenderPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.OfflineSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;

@Import({WebSettingsServiceConfiguration.class, MockTestConfiguration.class, OfflineSettingsConfiguration.class})
public class ExternalScriptsProcessingTestConfiguration {
	
	@Bean
	public AbstractElementPostProcessor externalScriptAppenderPostProcessor() {
		return new AcrisExternalScriptAppenderPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor externalScriptAnnihilatorPostProcessor() {
		return new AcrisExternalScriptAnnihilatorPostProcessor();
	}

}