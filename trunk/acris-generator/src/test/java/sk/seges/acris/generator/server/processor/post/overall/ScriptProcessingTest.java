package sk.seges.acris.generator.server.processor.post.overall;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.overall.ScriptProcessingTest.ScriptProcessingTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.overall.ScriptProcessingTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = ScriptProcessingTestConfigurationLoader.class)
public class ScriptProcessingTest extends AbstractProcessorTest {

	static class ScriptProcessingTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public ScriptProcessingTestConfigurationLoader() {
			super(ScriptProcessingTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/scripts/";

	@Test
	@DirtiesContext
	public void testScriptProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_script_input.html", 
				HTML_FILE_DIRECTORY + "1_test_script_result.html");
	}

}
