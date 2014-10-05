package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.alters.KeywordsMetaTagPostProcessorTest.KeyWordsTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.alters.KeywordsTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = KeyWordsTestConfigurationLoader.class) 
public class KeywordsMetaTagPostProcessorTest extends AbstractProcessorTest {

	static class KeyWordsTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {
		public KeyWordsTestConfigurationLoader() {
			super(KeywordsTestConfiguration.class);
		}
	}
	
	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/keywords/";

	@Test
	@DirtiesContext
	public void testKeywordsPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_keywords_input.html", 
				HTML_FILE_DIRECTORY + "1_test_keywords_result.html");
	}
	

	@Test
	@DirtiesContext
	public void testMissingKeywordsPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "2_test_keywords_missing_input.html", 
				HTML_FILE_DIRECTORY + "2_test_keywords_missing_result.html");
	}
}