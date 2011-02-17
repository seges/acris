package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractTest;
import sk.seges.acris.site.server.service.MockWebSettingsService;
import sk.seges.acris.site.server.service.builder.IWebSettingsBuilder;
import sk.seges.acris.site.server.service.builder.NoTopLevelDomainWebSettingsBuilder;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/sk/seges/acris/generator/niceurl-test-application-context.xml"})
public class NiceURLLinkPostProcessorTest extends AbstractTest {

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
	public void testNiceUrlComplexPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "4_test_niceurl_complex_input.html", 
				HTML_FILE_DIRECTORY + "4_test_niceurl_complex_result.html");
	}
}