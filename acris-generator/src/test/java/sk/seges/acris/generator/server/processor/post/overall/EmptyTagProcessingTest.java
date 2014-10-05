package sk.seges.acris.generator.server.processor.post.overall;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.overall.UTF8EncodingTest.UTF8EncodingTestConfigurationLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = UTF8EncodingTestConfigurationLoader.class)
public class EmptyTagProcessingTest extends AbstractProcessorTest {

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/empty/tags/";
	
	@Test
	@DirtiesContext
	public void testUTFHtmlEnconding() {
		runTest(HTML_FILE_DIRECTORY + "1_test_empty_tags_input.html", 
				HTML_FILE_DIRECTORY + "1_test_empty_tags_result.html");
	}
}