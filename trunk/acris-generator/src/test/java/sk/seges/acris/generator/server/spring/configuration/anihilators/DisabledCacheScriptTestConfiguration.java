package sk.seges.acris.generator.server.spring.configuration.anihilators;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.MockContentFactory;
import sk.seges.acris.generator.server.processor.MockContentInfoProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.NochacheScriptAnnihilatorPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.MockTestConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.OfflineSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;
import sk.seges.acris.site.server.service.IWebSettingsServiceLocal;
import sk.seges.acris.site.server.service.MockWebSettingsService;
import sk.seges.acris.site.server.service.builder.IWebSettingsBuilder;
import sk.seges.acris.site.server.service.builder.NocacheScriptWebSettingsBuilder;
import sk.seges.acris.site.shared.domain.mock.MockContent;

@Import({WebSettingsServiceConfiguration.class, OfflineSettingsConfiguration.class})
public class DisabledCacheScriptTestConfiguration {

	class MockLightContentFactory implements MockContentFactory {

		@Override
		public ContentData constructMockContent() {
			return new MockContent();
		}
	}

	@Bean
	public IWebSettingsBuilder webSettingsBuilder() {
		return new NocacheScriptWebSettingsBuilder();
	}

	@Bean
	public IWebSettingsServiceLocal webSettingsService() {
		return new MockWebSettingsService(webSettingsBuilder(), MockTestConfiguration.MOCK_ANALYTICS_SCRIPT, false);
	}

	@Bean
	public ContentDataProvider contentInfoProvider() {
		return new MockContentInfoProvider(new MockLightContentFactory());
	}

	@Bean
	public AbstractElementPostProcessor nochacheScriptPostProcessor() {
		return new NochacheScriptAnnihilatorPostProcessor();
	}
}
