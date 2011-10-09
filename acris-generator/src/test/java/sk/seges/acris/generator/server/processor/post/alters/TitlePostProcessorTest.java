package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.alters.TitlePostProcessorTest.TitlePostProcessorTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.alters.TitleTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = TitlePostProcessorTestConfigurationLoader.class)
public class TitlePostProcessorTest extends AbstractProcessorTest {

	static class TitlePostProcessorTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public TitlePostProcessorTestConfigurationLoader() {
			super(TitleTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/title/";

	@Test
	@DirtiesContext
	public void testTitlePostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_title_input.html", 
				HTML_FILE_DIRECTORY + "1_test_title_result.html");
	}

	@Test
	@DirtiesContext
	public void testMissingTitlePostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "2_test_title_missing_input.html", 
				HTML_FILE_DIRECTORY + "2_test_title_missing_result.html");
	}
}