package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.alters.EmptyDescriptionMetaTagPostProcessorTest.EmptyDescriptionTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.alters.EmptyDescriptionTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = EmptyDescriptionTestConfigurationLoader.class)
public class EmptyDescriptionMetaTagPostProcessorTest extends AbstractProcessorTest {

	static class EmptyDescriptionTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {
		public EmptyDescriptionTestConfigurationLoader() {
			super(EmptyDescriptionTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/description/";

	@Test
	@DirtiesContext
	public void testEmptyDescriptionPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "3_test_empty_description_input.html", 
				HTML_FILE_DIRECTORY + "3_test_empty_description_result.html");
	}
}