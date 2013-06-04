package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.alters.StylesPathPostProcessorTest.StylesPathPostProcessorTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.alters.StylePathTestConfiguration;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = StylesPathPostProcessorTestConfigurationLoader.class)
public class StylesPathPostProcessorTest extends AbstractProcessorTest {

	static class StylesPathPostProcessorTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public StylesPathPostProcessorTestConfigurationLoader() {
			super(StylePathTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/stylepath/";

	@Test
	@DirtiesContext
	public void testStylePathPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_stylepath_input.html", 
				HTML_FILE_DIRECTORY + "1_test_stylepath_result.html");
	}

	@Test
	@DirtiesContext
	public void testStylePathNestedPostProcessor() {
		GeneratorToken token = new GeneratorToken();
		token.setLanguage("en");
		token.setNiceUrl("lang/en/test");
		token.setWebId("test.sk");

		runTest(HTML_FILE_DIRECTORY + "2_test_stylepath_nested_input.html", 
				HTML_FILE_DIRECTORY + "2_test_stylepath_nested_result.html", token);
	}
}