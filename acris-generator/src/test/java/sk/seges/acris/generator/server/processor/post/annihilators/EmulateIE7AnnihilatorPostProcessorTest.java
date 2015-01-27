package sk.seges.acris.generator.server.processor.post.annihilators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.annihilators.EmulateIE7AnnihilatorPostProcessorTest.EmulateIE7AnnihilatorConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.anihilators.EmulateIE7TagTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = EmulateIE7AnnihilatorConfigurationLoader.class)
public class EmulateIE7AnnihilatorPostProcessorTest extends AbstractProcessorTest {

	static class EmulateIE7AnnihilatorConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public EmulateIE7AnnihilatorConfigurationLoader() {
			super(EmulateIE7TagTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/emulateie7/";

	@Test
	@DirtiesContext
	public void testEmulateIE7EnclosedTagPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_emulateie7_script_input.html", 
				HTML_FILE_DIRECTORY + "1_emulateie7_script_result.html");
	}

	@Test
	@DirtiesContext
	public void testEmulateIE7TagPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "2_emulateie7_script_input.html", 
				HTML_FILE_DIRECTORY + "2_emulateie7_script_result.html");
	}
}