package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.rpc.domain.GeneratorToken;
import sk.seges.acris.generator.server.processor.post.AbstractTest;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/sk/seges/acris/generator/scriptpath-test-application-context.xml"})
public class ScriptPathPostProcessorTest extends AbstractTest {

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/scriptpath/";

	@Test
	public void testTitlePostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_scriptpath_input.html", 
				HTML_FILE_DIRECTORY + "1_test_scriptpath_result.html");
	}

	@Test
	public void testMissingTitlePostProcessor() {
		GeneratorToken token = new GeneratorToken();
		token.setLanguage("en");
		token.setNiceUrl("lang/en/test");
		token.setWebId("test.sk");

		runTest(HTML_FILE_DIRECTORY + "2_test_scriptpath_nested_input.html", 
				HTML_FILE_DIRECTORY + "2_test_scriptpath_nested_result.html", token);
	}
}