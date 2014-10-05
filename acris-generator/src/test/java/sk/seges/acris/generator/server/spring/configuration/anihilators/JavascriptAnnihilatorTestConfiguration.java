package sk.seges.acris.generator.server.spring.configuration.anihilators;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.JavascriptAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.OfflineSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;

@Import({WebSettingsServiceConfiguration.class, MockTestConfiguration.class, OfflineSettingsConfiguration.class})
public class JavascriptAnnihilatorTestConfiguration {

	@Bean
	public AbstractElementPostProcessor javascriptAnnihilatorPostProcessor() {
		return new JavascriptAnnihilatorPostProcessor();
	}
}
