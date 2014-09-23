package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.alters.LanguageSelectorPostProcessorTest.LanguageSelectorPostProcessorTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.alters.LanguageSelectorTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = LanguageSelectorPostProcessorTestConfigurationLoader.class)
public class LanguageSelectorPostProcessorTest extends AbstractProcessorTest {

	static class LanguageSelectorPostProcessorTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public LanguageSelectorPostProcessorTestConfigurationLoader() {
			super(LanguageSelectorTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/languageselector/";

	@Test
	@DirtiesContext
	public void testLanguageSelectorPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_languageselector_input.html", 
				HTML_FILE_DIRECTORY + "1_test_languageselector_result.html");
	}

	@Test
	@DirtiesContext
	public void testComplexLanguageSelectorPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "2_test_languageselector_input.html", 
				HTML_FILE_DIRECTORY + "2_test_languageselector_result.html");
	}
	
	@Test
	@DirtiesContext
	public void testAnchorLanguageSelectorPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "3_test_languageselector_input.html", 
				HTML_FILE_DIRECTORY + "3_test_languageselector_result.html");
	}
	
	@Test
	@DirtiesContext
	public void testImageLanguageSelectorPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "4_test_languageselector_input.html", 
				HTML_FILE_DIRECTORY + "4_test_languageselector_result.html");
	}
}