package sk.seges.acris.generator.server.processor.post.overall;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.overall.UTF8EncodingTest.UTF8EncodingTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.overall.ScriptProcessingTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = UTF8EncodingTestConfigurationLoader.class)
public class UTF8EncodingTest extends AbstractProcessorTest {

	static class UTF8EncodingTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public UTF8EncodingTestConfigurationLoader() {
			super(ScriptProcessingTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/encoding/";

	@Test
	@DirtiesContext
	public void testHtmlEnconding() {
		runTest(HTML_FILE_DIRECTORY + "1_utf-8_encoding_input.html", 
				HTML_FILE_DIRECTORY + "1_utf-8_encoding_result.html");
	}

}
