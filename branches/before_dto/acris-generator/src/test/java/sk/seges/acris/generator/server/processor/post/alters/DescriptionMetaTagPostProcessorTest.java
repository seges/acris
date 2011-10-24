package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.alters.DescriptionMetaTagPostProcessorTest.DescriptionTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.alters.DescriptionTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = DescriptionTestConfigurationLoader.class)
public class DescriptionMetaTagPostProcessorTest extends AbstractProcessorTest {

	static class DescriptionTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {
		public DescriptionTestConfigurationLoader() {
			super(DescriptionTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/description/";

	@Test
	@DirtiesContext
	public void testDescriptionPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_description_input.html", 
				HTML_FILE_DIRECTORY + "1_test_description_result.html");
	}

	@Test
	@DirtiesContext
	public void testMissingDescriptionPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "2_test_description_missing_input.html", 
				HTML_FILE_DIRECTORY + "2_test_description_missing_result.html");
	}
}