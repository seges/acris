package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.generator.server.processor.post.AbstractTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/sk/seges/acris/generator/imagepath-test-application-context.xml"})
public class ImagePathPostProcessorTest extends AbstractTest {

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/imagepath/";

	@Test
	public void testImagePathPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_imagepath_input.html", 
				HTML_FILE_DIRECTORY + "1_test_imagepath_result.html");
	}

	@Test
	public void testImagePathNestedPostProcessor() {
		GeneratorToken token = new GeneratorToken();
		token.setLanguage("en");
		token.setNiceUrl("lang/en/test");
		token.setWebId("test.sk");

		runTest(HTML_FILE_DIRECTORY + "2_test_imagepath_nested_input.html", 
				HTML_FILE_DIRECTORY + "2_test_imagepath_nested_result.html", token);
	}
}