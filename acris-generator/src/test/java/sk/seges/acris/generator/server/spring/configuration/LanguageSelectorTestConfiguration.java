package sk.seges.acris.generator.server.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.LanguageSelectorPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.FullPostProcessingConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;

@Import({WebSettingsServiceConfiguration.class, MockTestConfiguration.class, FullPostProcessingConfiguration.class})
public class LanguageSelectorTestConfiguration {

	@Autowired
	private ContentDataProvider contentDataProvider;

	@Bean
	public AbstractElementPostProcessor languageSelectorPostProcessor() {
		return new LanguageSelectorPostProcessor(contentDataProvider);
	}
}