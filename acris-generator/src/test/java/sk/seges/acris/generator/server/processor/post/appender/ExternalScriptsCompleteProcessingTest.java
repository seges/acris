package sk.seges.acris.generator.server.processor.post.appender;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.spring.configuration.appender.ScriptPathPostprocessorAlterTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = ExternalScriptsCompleteProcessingTest.ExternalScriptsProcessingTestConfigurationLoader.class)
public class ExternalScriptsCompleteProcessingTest extends AbstractProcessorTest {

	static class ExternalScriptsProcessingTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public ExternalScriptsProcessingTestConfigurationLoader() {
			super(ScriptPathPostprocessorAlterTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/external/scripts/";
	
    @Test
    @DirtiesContext
    public void testExistingScript() {
        runTest(HTML_FILE_DIRECTORY + "4_external_scripts_input.html",
                HTML_FILE_DIRECTORY + "4_external_scripts_output.html");
    }
}