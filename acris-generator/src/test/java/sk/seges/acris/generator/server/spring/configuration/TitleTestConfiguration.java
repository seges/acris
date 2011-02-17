package sk.seges.acris.generator.server.spring.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.TitlePostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.TitleAppenderPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;
import sk.seges.acris.site.shared.service.IWebSettingsService;

@Import({WebSettingsServiceConfiguration.class, MockTestConfiguration.class})
public class TitleTestConfiguration {

	@Autowired
	private IWebSettingsService webSettingsService;

	@Autowired
	private ContentDataProvider contentDataProvider;

	@Bean
	public AbstractElementPostProcessor titlePostProcessor() {
		return new TitlePostProcessor(webSettingsService, contentDataProvider);
	}

	@Bean
	public AbstractElementPostProcessor titlePostProcessorAppender() {
		return new TitleAppenderPostProcessor(webSettingsService, contentDataProvider);
	}
}