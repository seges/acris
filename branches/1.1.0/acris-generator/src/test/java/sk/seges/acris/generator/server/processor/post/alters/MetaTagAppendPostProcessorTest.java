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
@ContextConfiguration(locations = {"classpath:/sk/seges/acris/generator/metatag-append-test-application-context.xml"})
public class MetaTagAppendPostProcessorTest extends AbstractTest {

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/metatag/";

	@Test
	public void testMetaDataPostProcessor() {
		IWebSettingsBuilder webSettingsBuilder = ((MockWebSettingsService)webSettingsService).getWebSettingsBuilder();
		((MockWebSettingsService)webSettingsService).setWebSettingsBuilder(new MetaTagWebSettingsBuilder());
		runTest(HTML_FILE_DIRECTORY + "3_test_metatag_append_input.html", 
				HTML_FILE_DIRECTORY + "3_test_metatag_append_result.html");
		((MockWebSettingsService)webSettingsService).setWebSettingsBuilder(webSettingsBuilder);
	}	
}