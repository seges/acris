package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractTest;
import sk.seges.acris.site.server.service.MockWebSettingsService;
import sk.seges.acris.site.server.service.builder.IWebSettingsBuilder;
import sk.seges.acris.site.server.service.builder.MetaTagWebSettingsBuilder;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/sk/seges/acris/generator/metatag-test-application-context.xml"})
public class MetaTagPostProcessorTest extends AbstractTest {

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/metatag/";

	@Test
	public void testMetaDataPostProcessor() {
		IWebSettingsBuilder webSettingsBuilder = ((MockWebSettingsService)webSettingsService).getWebSettingsBuilder();
		((MockWebSettingsService)webSettingsService).setWebSettingsBuilder(new MetaTagWebSettingsBuilder());
		runTest(HTML_FILE_DIRECTORY + "1_test_metatag_input.html", 
				HTML_FILE_DIRECTORY + "1_test_metatag_result.html");
		((MockWebSettingsService)webSettingsService).setWebSettingsBuilder(webSettingsBuilder);
	}
	
	@Test
	public void testMoreMetaDataPostProcessor() {
		IWebSettingsBuilder webSettingsBuilder = ((MockWebSettingsService)webSettingsService).getWebSettingsBuilder();
		((MockWebSettingsService)webSettingsService).setWebSettingsBuilder(new MetaTagWebSettingsBuilder());
		runTest(HTML_FILE_DIRECTORY + "2_test_metatags_input.html", 
				HTML_FILE_DIRECTORY + "2_test_metatags_result.html");
		((MockWebSettingsService)webSettingsService).setWebSettingsBuilder(webSettingsBuilder);
	}
}