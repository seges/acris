package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.alters.GwtLocaleMetaTagTest.GwtLocaleMetaTagTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.appender.GwtLocaleMetaTagTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = GwtLocaleMetaTagTestConfigurationLoader.class)
public class GwtLocaleMetaTagTest extends AbstractProcessorTest {

	static class GwtLocaleMetaTagTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public GwtLocaleMetaTagTestConfigurationLoader() {
			super(GwtLocaleMetaTagTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/gwtlocale/";

	@Test
	@DirtiesContext
	public void testExistingMetaScriptPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_gwtlocale_script_input.html", 
				HTML_FILE_DIRECTORY + "1_gwtlocale_script_result.html");
	}

	@Test
	@DirtiesContext
	public void testmissingMetaScriptPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "2_gwtlocale_script_input.html", 
				HTML_FILE_DIRECTORY + "2_gwtlocale_script_result.html");
	}
}