package sk.seges.acris.generator.server.spring.configuration.common;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.factory.HtmlProcessorFactory;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.site.server.service.MockWebSettingsService;
import sk.seges.acris.site.server.service.builder.DefaultWebSettingsBuilder;
import sk.seges.acris.site.server.service.builder.IWebSettingsBuilder;
import sk.seges.acris.site.shared.service.IWebSettingsService;


public class WebSettingsServiceConfiguration {

	private static final String MOCK_ANALYTICS_SCRIPT = "<script>test analytics script</script>";

	@Autowired
	private ApplicationContext applicationContext;

	@Autowired
	private ContentDataProvider contentMetaDataProvider;
	
	@Bean
	public IWebSettingsBuilder webSettingsBuilder() {
		return new DefaultWebSettingsBuilder();
	}
	
	@Bean
	public IWebSettingsService webSettingsService() {
		return new MockWebSettingsService(webSettingsBuilder(), MOCK_ANALYTICS_SCRIPT, false);
	}
	
	@Bean
	public HtmlProcessorFactory htmlProcessorFactory() {
		Map<String, AbstractElementPostProcessor> abstractPostProcessors = this.applicationContext.getBeansOfType(AbstractElementPostProcessor.class);
		return new HtmlProcessorFactory(abstractPostProcessors.values(), contentMetaDataProvider);
	}
}