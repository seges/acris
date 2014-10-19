package sk.seges.acris.generator.server.spring.configuration.anihilators;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.JavascriptAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.*;

@Import({WebSettingsServiceConfiguration.class, WebSettingsConfiguration.class,
        MockTestConfiguration.class, OfflineSettingsConfiguration.class})
public class JavascriptAnnihilatorTestConfiguration {

	@Bean
	public AbstractElementPostProcessor javascriptAnnihilatorPostProcessor() {
		return new JavascriptAnnihilatorPostProcessor();
	}
}