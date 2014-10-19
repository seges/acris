package sk.seges.acris.generator.server.spring.configuration.alters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.ScriptsAlterPathPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.OfflineSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;

@Import({WebSettingsServiceConfiguration.class, WebSettingsConfiguration.class,
        MockTestConfiguration.class, OfflineSettingsConfiguration.class})
public class ScriptPathTestConfiguration {

	@Bean
	public AbstractElementPostProcessor scriptsPathPostProcessor() {
		return new ScriptsAlterPathPostProcessor();
	}
}