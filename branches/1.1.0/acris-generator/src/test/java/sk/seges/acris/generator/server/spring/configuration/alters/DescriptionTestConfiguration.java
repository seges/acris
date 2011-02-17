package sk.seges.acris.generator.server.spring.configuration.alters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.DescriptionMetaTagPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.DescriptionMetaTagAppenderPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;
import sk.seges.acris.site.shared.service.IWebSettingsService;

@Import({WebSettingsServiceConfiguration.class, MockTestConfiguration.class})
public class DescriptionTestConfiguration {

	@Autowired
	private IWebSettingsService webSettingsService;

	@Autowired
	private ContentDataProvider contentDataProvider;
	
	@Bean
	public AbstractElementPostProcessor descriptionMetaTagPostProcessor() {
		return new DescriptionMetaTagPostProcessor(webSettingsService, contentDataProvider);
	}
	
	@Bean
	public AbstractElementPostProcessor descriptionMetaTagAppenderPostProcessor() {
		return new DescriptionMetaTagAppenderPostProcessor(webSettingsService, contentDataProvider);
	}
}