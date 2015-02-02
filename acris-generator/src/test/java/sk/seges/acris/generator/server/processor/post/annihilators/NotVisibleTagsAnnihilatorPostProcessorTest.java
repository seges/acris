package sk.seges.acris.generator.server.processor.post.annihilators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.annihilators.NotVisibleTagsAnnihilatorPostProcessorTest.NotVisibleTagsTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.anihilators.NotVisibleTagsTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = NotVisibleTagsTestConfigurationLoader.class)
public class NotVisibleTagsAnnihilatorPostProcessorTest extends AbstractProcessorTest {

	static class NotVisibleTagsTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public NotVisibleTagsTestConfigurationLoader() {
			super(NotVisibleTagsTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/visibility/";

	@Test
	@DirtiesContext
	public void testHideInvisibleElementPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_hide_invisible_elements_input.html", 
				HTML_FILE_DIRECTORY + "1_test_hide_invisible_elements_result.html");
	}

	@Test
	@DirtiesContext
	public void testSkipHiddenChosenElementPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "2_test_skip_chosen_elements_input.html", 
				HTML_FILE_DIRECTORY + "2_test_skip_chosen_elements_result.html");
	}

	@Test
	@DirtiesContext
	public void testHiddenIframeElementPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "3_test_skip_hidden_iframe_input.html", 
				HTML_FILE_DIRECTORY + "3_test_skip_hidden_iframe_result.html");
	}

}