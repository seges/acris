package sk.seges.acris.generator.server.spring.configuration.anihilators;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.MockContentInfoProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.KeywordsMetaTagPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.KeywordsPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.KeywordsMetaTagAppenderPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.alters.EmptyKeywordsTestConfiguration.MockLightContentFactory;
import sk.seges.acris.generator.server.spring.configuration.common.OfflineSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;

@Import({WebSettingsServiceConfiguration.class, OfflineSettingsConfiguration.class})
public class EmptyKeywordsTestConfiguration {

	@Bean
	public AbstractElementPostProcessor keywordsMetaTagPostProcessor() {
		return new KeywordsMetaTagPostProcessor();
	}
	
	@Bean
	public AbstractElementPostProcessor keywordsPostProcessor() {
		return new KeywordsPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor keywordsMetaTagAppenderPostProcessor() {
		return new KeywordsMetaTagAppenderPostProcessor();
	}
	
	@Bean
	public ContentDataProvider contentInfoProvider() {
		return new MockContentInfoProvider(new MockLightContentFactory());
	}
}