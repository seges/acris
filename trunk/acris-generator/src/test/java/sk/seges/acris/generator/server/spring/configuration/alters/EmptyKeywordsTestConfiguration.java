package sk.seges.acris.generator.server.spring.configuration.alters;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.MockContentFactory;
import sk.seges.acris.generator.server.processor.MockContentInfoProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.KeywordsMetaTagAlterPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.KeywordsMetaTagAppenderPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.OfflineSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;
import sk.seges.acris.site.shared.domain.mock.MockContent;

@Import({WebSettingsServiceConfiguration.class, OfflineSettingsConfiguration.class})
public class EmptyKeywordsTestConfiguration {

	public static class MockLightContentFactory implements MockContentFactory {

		@SuppressWarnings("serial")
		@Override
		public ContentData constructMockContent() {
			return new MockContent() {

				@Override
				public String getKeywords() {
					return null;
				}
			};
		}
	}
	
	@Bean
	public AbstractElementPostProcessor keywordsMetaTagPostProcessor() {
		return new KeywordsMetaTagAlterPostProcessor();
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