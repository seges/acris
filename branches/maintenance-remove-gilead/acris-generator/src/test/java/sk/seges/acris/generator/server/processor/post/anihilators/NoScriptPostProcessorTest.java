package sk.seges.acris.generator.server.processor.post.anihilators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.anihilators.NoScriptPostProcessorTest.NoScriptTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.anihilators.NoScriptTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = NoScriptTestConfigurationLoader.class)
public class NoScriptPostProcessorTest extends AbstractProcessorTest {

	static class NoScriptTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public NoScriptTestConfigurationLoader() {
			super(NoScriptTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/noscript/";

	@Test
	@DirtiesContext
	public void testNoCacheScriptPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_no_script_script_input.html", 
				HTML_FILE_DIRECTORY + "1_no_script_script_result.html");
	}
}