package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.alters.ImageGalleryPostProcessorTest.ImageGalleryPostProcessorTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.alters.ImageGalleryTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = ImageGalleryPostProcessorTestConfigurationLoader.class)
public class ImageGalleryPostProcessorTest extends AbstractProcessorTest {
	
	static class ImageGalleryPostProcessorTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public ImageGalleryPostProcessorTestConfigurationLoader() {
			super(ImageGalleryTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/gallery/";

	@Test
	@DirtiesContext
	public void testImagePathPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_gallery_input.html", 
				HTML_FILE_DIRECTORY + "1_test_gallery_result.html");
	}

}
