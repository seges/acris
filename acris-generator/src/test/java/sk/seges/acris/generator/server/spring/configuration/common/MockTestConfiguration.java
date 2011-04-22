package sk.seges.acris.generator.server.spring.configuration.common;

import org.springframework.context.annotation.Bean;

import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.MockContentFactory;
import sk.seges.acris.generator.server.processor.MockContentInfoProvider;
import sk.seges.acris.site.server.service.MockWebSettingsService;
import sk.seges.acris.site.server.service.builder.DefaultWebSettingsBuilder;
import sk.seges.acris.site.server.service.builder.IWebSettingsBuilder;
import sk.seges.acris.site.shared.domain.mock.MockContent;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public class MockTestConfiguration {

	public static final String MOCK_ANALYTICS_SCRIPT = "<script>test analytics script</script>";

	class MockLightContentFactory implements MockContentFactory {

		@Override
		public ContentData<Long> constructMockContent() {
			return new MockContent();
		}
	}

	@Bean
	public IWebSettingsBuilder webSettingsBuilder() {
		return new DefaultWebSettingsBuilder();
	}

	@Bean
	public IWebSettingsService webSettingsService() {
		return new MockWebSettingsService(webSettingsBuilder(), MOCK_ANALYTICS_SCRIPT, false);
	}

	@Bean
	public ContentDataProvider contentInfoProvider() {
		return new MockContentInfoProvider(new MockLightContentFactory());
	}
}