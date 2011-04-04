package sk.seges.acris.generator.server.processor.post.anihilators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractTest;
import sk.seges.acris.generator.server.processor.post.anihilators.CacheScriptPostProcessorTest.CacheScriptTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.anihilators.CacheScriptTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = CacheScriptTestConfigurationLoader.class)
public class CacheScriptPostProcessorTest extends AbstractTest {

	static class CacheScriptTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public CacheScriptTestConfigurationLoader() {
			super(CacheScriptTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/cache/";

	@Test
	@DirtiesContext
	public void testCacheScriptPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_cache_script_input.html", 
				HTML_FILE_DIRECTORY + "1_cache_script_result.html");
	}
}