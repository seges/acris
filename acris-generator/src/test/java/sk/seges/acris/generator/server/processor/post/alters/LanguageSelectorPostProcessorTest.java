package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/sk/seges/acris/generator/languageselector-test-application-context.xml"})
public class LanguageSelectorPostProcessorTest extends AbstractTest {

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/languageselector/";

	@Test
	@DirtiesContext
	public void testLanguageSelectorPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_languageselector_input.html", 
				HTML_FILE_DIRECTORY + "1_test_languageselector_result.html");
	}

	@Test
	@DirtiesContext
	public void testComplexLanguageSelectorPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "2_test_languageselector_input.html", 
				HTML_FILE_DIRECTORY + "2_test_languageselector_result.html");
	}
}