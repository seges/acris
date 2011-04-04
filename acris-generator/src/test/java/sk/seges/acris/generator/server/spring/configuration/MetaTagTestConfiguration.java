package sk.seges.acris.generator.server.spring.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.MetaTagPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.MetaTagAppenderPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.FullPostProcessingConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;

@Import({WebSettingsServiceConfiguration.class, MockTestConfiguration.class, FullPostProcessingConfiguration.class})
public class MetaTagTestConfiguration {

	@Bean
	public AbstractElementPostProcessor metaTagPostProcessor() {
		return new MetaTagPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor metaTagAppenderPostProcessor() {
		return new MetaTagAppenderPostProcessor();
	}
}