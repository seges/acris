package sk.seges.acris.generator.server.processor.post.appender;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.spring.configuration.overall.ExternalScriptsProcessingTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

/**
 * Tests verifies whether script tags are correctly moved from
 * <div class="acris-external-scripts/> tag into the head element
 * Following cases should be covered:
 * <ul>
 * <li>head and external-scripts DIV tag are available</li>
 * <li>no head tag is available</li>
 * <li>same scripts in the head tag and in the external-scripts DIV are available</li>
 * </ul>
 * 
 * @author Peter Simun (simun@seges.sk)
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = ExternalScriptsProcessingTest.ExternalScriptsProcessingTestConfigurationLoader.class)
public class ExternalScriptsProcessingTest extends AbstractProcessorTest {

	static class ExternalScriptsProcessingTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public ExternalScriptsProcessingTestConfigurationLoader() {
			super(ExternalScriptsProcessingTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/external/scripts/";
	
	@Test
	@DirtiesContext
	public void testPositiveCase() {
		runTest(HTML_FILE_DIRECTORY + "1_external_scripts_input.html", 
				HTML_FILE_DIRECTORY + "1_external_scripts_output.html");
	}

	@Test
	@DirtiesContext
	public void testNoHead() {
		runTest(HTML_FILE_DIRECTORY + "2_external_scripts_input.html", 
				HTML_FILE_DIRECTORY + "2_external_scripts_output.html");
	}

	@Test
	@DirtiesContext
	public void testExistingScript() {
		runTest(HTML_FILE_DIRECTORY + "3_external_scripts_input.html", 
				HTML_FILE_DIRECTORY + "3_external_scripts_output.html");
	}
}