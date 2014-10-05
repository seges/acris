package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.alters.ScriptPathPostProcessorTest.ScriptPathPostProcessorTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.alters.ScriptPathTestConfiguration;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = ScriptPathPostProcessorTestConfigurationLoader.class)
public class ScriptPathPostProcessorTest extends AbstractProcessorTest {

	static class ScriptPathPostProcessorTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public ScriptPathPostProcessorTestConfigurationLoader() {
			super(ScriptPathTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/scriptpath/";

	@Test
	@DirtiesContext
	public void testScriptPathPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_scriptpath_input.html", 
				HTML_FILE_DIRECTORY + "1_test_scriptpath_result.html");
	}

	@Test
	@DirtiesContext
	public void testNestedScriptPathPostProcessor() {
		GeneratorToken token = new GeneratorToken();
		token.setLanguage("en");
		token.setNiceUrl("lang/en/test");
		token.setWebId("test.sk");

		runTest(HTML_FILE_DIRECTORY + "2_test_scriptpath_nested_input.html", 
				HTML_FILE_DIRECTORY + "2_test_scriptpath_nested_result.html", token);
	}
}