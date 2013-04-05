package sk.seges.acris.generator.server.processor.post.anihilators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.anihilators.ClearCacheImagePostProcessorTest.ClearCacheImagePostProcessorTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.anihilators.ClearCacheImagePostProcessorTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = ClearCacheImagePostProcessorTestConfigurationLoader.class)
public class ClearCacheImagePostProcessorTest extends AbstractProcessorTest {

	static class ClearCacheImagePostProcessorTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public ClearCacheImagePostProcessorTestConfigurationLoader() {
			super(ClearCacheImagePostProcessorTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/clearcache/";

	@Test
	@DirtiesContext
	public void testCacheScriptPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_clearcache_input.html", 
				HTML_FILE_DIRECTORY + "1_test_clearcache_result.html");
	}
}