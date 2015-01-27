package sk.seges.acris.generator.server.processor.post.annihilators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.annihilators.DisabledCacheScriptPostProcessorTest.DisabledCacheScriptTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.anihilators.DisabledCacheScriptTestConfiguration;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = DisabledCacheScriptTestConfigurationLoader.class)
public class DisabledCacheScriptPostProcessorTest extends AbstractProcessorTest {

	static class DisabledCacheScriptTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public DisabledCacheScriptTestConfigurationLoader() {
			super(DisabledCacheScriptTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/disabledcache/";

	@Test
	@DirtiesContext
	public void testCacheScriptPostProcessor() {
        GeneratorToken token = new GeneratorToken();
        token.setLanguage("en");
        token.setNiceUrl("test");
        token.setWebId("www.seges.sk");
        token.setDefaultToken(true);

		runTest(HTML_FILE_DIRECTORY + "1_cache_script_input.html",
				HTML_FILE_DIRECTORY + "1_cache_script_result.html", token);
	}

	@Test
	@DirtiesContext
	public void testCacheIndexScriptPostProcessor() {
		GeneratorToken token = new GeneratorToken();
		token.setLanguage("en");
		token.setNiceUrl("test");
		token.setWebId("www.seges.sk");
        token.setDefaultToken(false);
		
		runTest(HTML_FILE_DIRECTORY + "2_cache_script_index_input.html", 
				HTML_FILE_DIRECTORY + "2_cache_script_index_result.html", token);
	}
}