package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.alters.ImagePathPostProcessorTest.ImagePathPostProcessorTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.alters.ImagePathTestConfiguration;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = ImagePathPostProcessorTestConfigurationLoader.class)
public class ImagePathPostProcessorTest extends AbstractProcessorTest {

	static class ImagePathPostProcessorTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public ImagePathPostProcessorTestConfigurationLoader() {
			super(ImagePathTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/imagepath/";

	@Test
	@DirtiesContext
	public void testImagePathPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_imagepath_input.html", 
				HTML_FILE_DIRECTORY + "1_test_imagepath_result.html");
	}

	@Test
	@DirtiesContext
	public void testImagePathNestedPostProcessor() {
		GeneratorToken token = new GeneratorToken();
		token.setLanguage("en");
		token.setNiceUrl("lang/en/test");
		token.setWebId("test.sk");

		runTest(HTML_FILE_DIRECTORY + "2_test_imagepath_nested_input.html", 
				HTML_FILE_DIRECTORY + "2_test_imagepath_nested_result.html", token);
	}
}