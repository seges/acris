package sk.seges.acris.generator.server.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.DescriptionMetaTagAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.DescriptionMetaTagAppenderPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.*;

@Import({WebSettingsServiceConfiguration.class, WebSettingsConfiguration.class, MockTestConfiguration.class,
        JSONOfflineSettingsConfiguration.class, CombinedModeConfiguration.class})
public class JSONTestConfiguration {

	@Bean
	public AbstractElementPostProcessor descriptionMetaTagPostProcessor() {
		return new DescriptionMetaTagAlterPostProcessor();
	}
	
	@Bean
	public AbstractElementPostProcessor descriptionMetaTagAppenderPostProcessor() {
		return new DescriptionMetaTagAppenderPostProcessor();
	}
}