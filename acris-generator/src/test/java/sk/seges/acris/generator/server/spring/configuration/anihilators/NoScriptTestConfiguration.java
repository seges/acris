package sk.seges.acris.generator.server.spring.configuration.anihilators;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.NoscriptAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.*;

@Import({WebSettingsServiceConfiguration.class, WebSettingsConfiguration.class,
        MockTestConfiguration.class, OfflineSettingsConfiguration.class,
        OfflineModeConfiguration.class})
public class NoScriptTestConfiguration {

	@Bean
	public AbstractElementPostProcessor nochacheScriptPostProcessor() {
		return new NoscriptAnnihilatorPostProcessor();
	}
}