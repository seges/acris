package sk.seges.acris.generator.server.spring.configuration.anihilators;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import sk.seges.acris.domain.shared.domain.api.ContentData;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.MockContentFactory;
import sk.seges.acris.generator.server.processor.MockContentInfoProvider;
import sk.seges.acris.generator.server.processor.post.AbstractElementPostProcessor;
import sk.seges.acris.generator.server.processor.post.alters.DescriptionMetaTagPostProcessor;
import sk.seges.acris.generator.server.processor.post.annihilators.DescriptionPostProcessor;
import sk.seges.acris.generator.server.processor.post.appenders.DescriptionMetaTagAppenderPostProcessor;
import sk.seges.acris.generator.server.spring.configuration.common.OfflineSettingsConfiguration;
import sk.seges.acris.generator.server.spring.configuration.common.WebSettingsServiceConfiguration;
import sk.seges.acris.site.shared.domain.mock.MockContent;

@Import({WebSettingsServiceConfiguration.class, OfflineSettingsConfiguration.class})
public class EmptyDescriptionTestConfiguration {

	public static class MockLightContentFactory implements MockContentFactory {

		@SuppressWarnings("serial")
		@Override
		public ContentData<Long> constructMockContent() {
			return new MockContent() {
				@Override
				public String getDescription() {
					return null;
				}
			};
		}
	}

	@Bean
	public AbstractElementPostProcessor descriptionMetaTagPostProcessor() {
		return new DescriptionMetaTagPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor descriptionMetaTagAnnihilatorPostProcessor() {
		return new DescriptionPostProcessor();
	}

	@Bean
	public AbstractElementPostProcessor descriptionMetaTagAppenderPostProcessor() {
		return new DescriptionMetaTagAppenderPostProcessor();
	}
	
	@Bean
	public ContentDataProvider contentInfoProvider() {
		return new MockContentInfoProvider(new MockLightContentFactory());
	}
}