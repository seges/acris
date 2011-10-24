package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.alters.NiceURLLinkPostProcessorTest.NiceURLLinkPostProcessorTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.alters.NiceurlPathTestConfiguration;
import sk.seges.acris.site.server.service.MockWebSettingsService;
import sk.seges.acris.site.server.service.builder.IWebSettingsBuilder;
import sk.seges.acris.site.server.service.builder.NoTopLevelDomainWebSettingsBuilder;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = NiceURLLinkPostProcessorTestConfigurationLoader.class)
public class NiceURLLinkPostProcessorTest extends AbstractProcessorTest {

	static class NiceURLLinkPostProcessorTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public NiceURLLinkPostProcessorTestConfigurationLoader() {
			super(NiceurlPathTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/niceurl/";

	@Test
	@DirtiesContext
	public void testNiceUrlPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_niceurl_input.html", 
				HTML_FILE_DIRECTORY + "1_test_niceurl_result.html");
	}

	@Test
	@DirtiesContext
	public void testNiceUrlHashPostProcessor() {
		IWebSettingsBuilder webSettingsBuilder = ((MockWebSettingsService)webSettingsService).getWebSettingsBuilder();
		((MockWebSettingsService)webSettingsService).setWebSettingsBuilder(new NoTopLevelDomainWebSettingsBuilder());
		runTest(HTML_FILE_DIRECTORY + "2_test_niceurl_hash_input.html", 
				HTML_FILE_DIRECTORY + "2_test_niceurl_hash_result.html");
		((MockWebSettingsService)webSettingsService).setWebSettingsBuilder(webSettingsBuilder);
	}

	@Test
	@DirtiesContext
	public void testNiceUrlAbsolutePostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "3_test_niceurl_absolute_input.html", 
				HTML_FILE_DIRECTORY + "3_test_niceurl_absolute_result.html");
	}

	@Test
	@DirtiesContext
	public void testNiceUrlComplexPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "4_test_niceurl_complex_input.html", 
				HTML_FILE_DIRECTORY + "4_test_niceurl_complex_result.html");
	}
}