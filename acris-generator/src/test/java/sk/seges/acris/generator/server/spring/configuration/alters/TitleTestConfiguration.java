package sk.seges.acris.generator.server.spring.configuration.alters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.TitlePostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.TitleAppenderPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.OfflineSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;

@Import({WebSettingsServiceConfiguration.class, MockTestConfiguration.class, OfflineSettingsConfiguration.class})
public class TitleTestConfiguration {

	@Bean
	public AbstractElementPostProcessor titlePostProcessor() {
		return new TitlePostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor titlePostProcessorAppender() {
		return new TitleAppenderPostProcessor();
	}
}