package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.alters.EmptyTagAttributePostProcessorTest.EmptyTagAttributeTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.alters.EmptyTagAttributeTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = EmptyTagAttributeTestConfigurationLoader.class)
public class EmptyTagAttributePostProcessorTest extends AbstractProcessorTest {

	static class EmptyTagAttributeTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {
		public EmptyTagAttributeTestConfigurationLoader() {
			super(EmptyTagAttributeTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/attribute/";

	@Test
	@DirtiesContext
	public void testEmptyAttributePostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_empty_attribute_input.html", 
				HTML_FILE_DIRECTORY + "1_test_empty_attribute_result.html");
	}
}