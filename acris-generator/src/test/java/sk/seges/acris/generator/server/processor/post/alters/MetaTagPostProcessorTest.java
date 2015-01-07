package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.alters.MetaTagPostProcessorTest.MetaTagPostProcessorTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.alters.MetaTagTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = MetaTagPostProcessorTestConfigurationLoader.class)
public class MetaTagPostProcessorTest extends AbstractProcessorTest {

	static class MetaTagPostProcessorTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public MetaTagPostProcessorTestConfigurationLoader() {
			super(MetaTagTestConfiguration.class);
		}
	}
	
	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/metatag/";

	@Test
	@DirtiesContext
	public void testMetaDataPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_metatag_input.html",
                HTML_FILE_DIRECTORY + "1_test_metatag_result.html");
	}
	
	@Test
	@DirtiesContext
	public void testMoreMetaDataPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "2_test_metatags_input.html",
                HTML_FILE_DIRECTORY + "2_test_metatags_result.html");
	}
	
	@Test
	@DirtiesContext
	public void testMetaDataAppendPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "3_test_metatag_append_input.html",
                HTML_FILE_DIRECTORY + "3_test_metatag_append_result.html");
	}
}