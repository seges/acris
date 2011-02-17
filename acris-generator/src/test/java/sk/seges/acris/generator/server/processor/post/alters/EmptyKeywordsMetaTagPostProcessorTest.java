package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractTest;
import sk.seges.acris.generator.server.processor.post.alters.EmptyKeywordsMetaTagPostProcessorTest.EmptyKeyWordsTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.alters.EmptyKeywordsTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = EmptyKeyWordsTestConfigurationLoader.class)
public class EmptyKeywordsMetaTagPostProcessorTest extends AbstractTest {

	static class EmptyKeyWordsTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public EmptyKeyWordsTestConfigurationLoader() {
			super(EmptyKeywordsTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/keywords/";

	@Test
	@DirtiesContext
	public void testEmptyKeywordsPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "3_test_empty_keywords_input.html", 
				HTML_FILE_DIRECTORY + "3_test_empty_keywords_result.html");
	}
}