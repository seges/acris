package sk.seges.acris.generator.server.spring.configuration.anihilators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.MockContentInfoProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.KeywordsMetaTagPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.KeywordsPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.KeywordsMetaTagAppenderPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.alters.EmptyKeywordsTestConfiguration.MockLightContentFactory;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;
import sk.seges.acris.site.shared.service.IWebSettingsService;

@Import({WebSettingsServiceConfiguration.class})
public class EmptyKeywordsTestConfiguration {

	@Autowired
	private IWebSettingsService webSettingsService;

	@Autowired
	private ContentDataProvider contentDataProvider;

	@Bean
	public AbstractElementPostProcessor keywordsMetaTagPostProcessor() {
		return new KeywordsMetaTagPostProcessor(webSettingsService, contentDataProvider);
	}
	
	@Bean
	public AbstractElementPostProcessor keywordsPostProcessor() {
		return new KeywordsPostProcessor(webSettingsService, contentDataProvider);
	}

	@Bean
	public AbstractElementPostProcessor keywordsMetaTagAppenderPostProcessor() {
		return new KeywordsMetaTagAppenderPostProcessor(webSettingsService, contentDataProvider);
	}
	
	@Bean
	public ContentDataProvider contentInfoProvider() {
		return new MockContentInfoProvider(new MockLightContentFactory());
	}
}