package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/sk/seges/acris/generator/token-test-application-context.xml"})
public class TitlePostProcessorTest extends AbstractTest {

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/title/";

	@Test
	public void testTitlePostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_title_input.html", 
				HTML_FILE_DIRECTORY + "1_test_title_result.html");
	}

	@Test
	public void testMissingTitlePostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "2_test_title_missing_input.html", 
				HTML_FILE_DIRECTORY + "2_test_title_missing_result.html");
	}
}