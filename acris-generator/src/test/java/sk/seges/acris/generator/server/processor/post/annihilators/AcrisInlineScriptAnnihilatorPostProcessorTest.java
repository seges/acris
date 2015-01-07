package sk.seges.acris.generator.server.processor.post.annihilators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.annihilators.AcrisInlineScriptAnnihilatorPostProcessorTest.AcrisInlineScriptAnnihilatorTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.anihilators.AcrisInlineScriptAnnihilatorTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = AcrisInlineScriptAnnihilatorTestConfigurationLoader.class)
public class AcrisInlineScriptAnnihilatorPostProcessorTest extends AbstractProcessorTest {

	static class AcrisInlineScriptAnnihilatorTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public AcrisInlineScriptAnnihilatorTestConfigurationLoader() {
			super(AcrisInlineScriptAnnihilatorTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/inlinescripts/";

	@Test
	@DirtiesContext
	public void testInlineScriptPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_inline_script_input.html", 
				HTML_FILE_DIRECTORY + "1_inline_script_result.html");
	}

	@Test
	@DirtiesContext
	public void testMultipleInlineScriptPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "2_multiple_inline_script_input.html", 
				HTML_FILE_DIRECTORY + "2_multiple_inline_script_result.html");
	}
}