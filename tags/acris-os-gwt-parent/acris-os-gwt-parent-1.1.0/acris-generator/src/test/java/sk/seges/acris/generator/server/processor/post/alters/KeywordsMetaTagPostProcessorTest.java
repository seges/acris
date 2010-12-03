package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractTest;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/sk/seges/acris/generator/keywords-test-application-context.xml"})
public class KeywordsMetaTagPostProcessorTest extends AbstractTest {

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/keywords/";

	@Test
	public void testKeywordsPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_keywords_input.html", 
				HTML_FILE_DIRECTORY + "1_test_keywords_result.html");
	}

	@Test
	public void testMissingKeywordsPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "2_test_keywords_missing_input.html", 
				HTML_FILE_DIRECTORY + "2_test_keywords_missing_result.html");
	}
}