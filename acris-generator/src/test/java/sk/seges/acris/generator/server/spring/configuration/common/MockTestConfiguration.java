package sk.seges.acris.generator.server.spring.configuration.common;

import org.springframework.context.annotation.Bean;

import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.MockContentFactory;
import sk.seges.acris.generator.server.processor.MockContentInfoProvider;
import sk.seges.acris.site.server.domain.api.ContentData;
import sk.seges.acris.site.shared.domain.mock.MockContent;

public class MockTestConfiguration {

	public static final String MOCK_ANALYTICS_SCRIPT = "<script>test analytics script</script>";

	class MockLightContentFactory implements MockContentFactory {

		@Override
		public ContentData constructMockContent() {
			return new MockContent();
		}
	}

	@Bean
	public ContentDataProvider contentInfoProvider() {
		return new MockContentInfoProvider(new MockLightContentFactory());
	}
}