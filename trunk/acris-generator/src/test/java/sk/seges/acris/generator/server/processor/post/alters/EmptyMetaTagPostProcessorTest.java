package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.alters.EmptyMetaTagPostProcessorTest.EmptyMetaTagPostProcessorTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.alters.EmptyMetaTagTestConfiguration;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.site.server.service.MockWebSettingsService;
import sk.seges.acris.site.server.service.builder.IWebSettingsBuilder;
import sk.seges.acris.site.server.service.builder.MetaTagWebSettingsBuilder;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = EmptyMetaTagPostProcessorTestConfigurationLoader.class)
public class EmptyMetaTagPostProcessorTest extends AbstractProcessorTest {

	static class EmptyMetaTagPostProcessorTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public EmptyMetaTagPostProcessorTestConfigurationLoader() {
			super(EmptyMetaTagTestConfiguration.class);
		}
	}
	
	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/metatag/";
	
	@Test
	@DirtiesContext
	public void testEmptyMetaTagPostProcessor() {
		IWebSettingsBuilder webSettingsBuilder = ((MockWebSettingsService)webSettingsService).getWebSettingsBuilder();
		((MockWebSettingsService)webSettingsService).setWebSettingsBuilder(new MetaTagWebSettingsBuilder());
				
		runTest(HTML_FILE_DIRECTORY + "4_test_empty_metatag_input.html", 
				HTML_FILE_DIRECTORY + "4_test_empty_metatag_result.html", getHomeToken(), true);
		((MockWebSettingsService)webSettingsService).setWebSettingsBuilder(webSettingsBuilder);
	}

	protected GeneratorToken getHomeToken() {
		GeneratorToken token = new GeneratorToken();
		token.setLanguage("sk");
		token.setNiceUrl("sk/home-page");
		token.setWebId("montre.sk");
		return token;
	}

}