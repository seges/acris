package sk.seges.acris.generator.server.spring.configuration;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import sk.seges.acris.generator.server.processor.HtmlPostProcessing;
import sk.seges.acris.generator.server.processor.IContentInfoProvider;
import sk.seges.acris.generator.server.processor.MockContentInfoProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.site.server.service.MockWebSettingsService;
import sk.seges.acris.site.shared.service.IWebSettingsService;


public class WebSettingsServiceConfiguration {

	private static final String MOCK_ANALYTICS_SCRIPT = "<script>test analytics script</script>";

	@Autowired
	private ApplicationContext applicationContext;

	@Bean
	public HtmlPostProcessing htmlPostProcessing() {
		Map<String, AbstractElementPostProcessor> abstractPostProcessors = this.applicationContext.getBeansOfType(AbstractElementPostProcessor.class);
		return new HtmlPostProcessing(abstractPostProcessors.values());
	}

	@Bean
	public IWebSettingsService webSettingsService() {
		return new MockWebSettingsService(MOCK_ANALYTICS_SCRIPT, false);
	}
	
	@Bean
	public IContentInfoProvider contentInfoProvider() {
		return new MockContentInfoProvider();
	}
}