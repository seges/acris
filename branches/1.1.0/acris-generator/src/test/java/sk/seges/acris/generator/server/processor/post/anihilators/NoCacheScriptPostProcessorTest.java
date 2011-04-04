package sk.seges.acris.generator.server.processor.post.anihilators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractTest;
import sk.seges.acris.generator.server.processor.post.anihilators.NoCacheScriptPostProcessorTest.NoCacheScriptTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.anihilators.NoCacheScriptTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = NoCacheScriptTestConfigurationLoader.class)
public class NoCacheScriptPostProcessorTest extends AbstractTest {

	static class NoCacheScriptTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public NoCacheScriptTestConfigurationLoader() {
			super(NoCacheScriptTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/nocache/";

	@Test
	@DirtiesContext
	public void testNoCacheScriptPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_no_cache_script_input.html", 
				HTML_FILE_DIRECTORY + "1_no_cache_script_result.html");
	}
}