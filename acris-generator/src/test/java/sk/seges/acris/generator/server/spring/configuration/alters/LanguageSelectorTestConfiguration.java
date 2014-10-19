package sk.seges.acris.generator.server.spring.configuration.alters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.ImageLanguageSelectorAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.LinkLanguageSelectorAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.NiceURLLinkAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.SelectLanguageSelectorAlterPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.OfflineSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;

@Import({WebSettingsServiceConfiguration.class, WebSettingsConfiguration.class,
        MockTestConfiguration.class, OfflineSettingsConfiguration.class})
public class LanguageSelectorTestConfiguration {

	@Autowired
	private ContentDataProvider contentDataProvider;

	@Bean
	public AbstractElementPostProcessor imageLanguageSelectorAlterPostProcessor() {
		return new ImageLanguageSelectorAlterPostProcessor(contentDataProvider);
	}

	@Bean
	public AbstractElementPostProcessor linkLanguageSelectorAlterPostProcessor() {
		return new LinkLanguageSelectorAlterPostProcessor(contentDataProvider);
	}

	@Bean
	public AbstractElementPostProcessor selectLanguageSelectorAlterPostProcessor() {
		return new SelectLanguageSelectorAlterPostProcessor(contentDataProvider);
	}
	
	@Bean
	public AbstractElementPostProcessor niceURLLinkAlterPostProcessor() {
		return new NiceURLLinkAlterPostProcessor();
	}
}