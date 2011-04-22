package sk.seges.acris.generator.server.processor.post.overall;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.overall.NestedLinkElementsPostProcessorTest.NestedLinkElementsPostProcessorTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.overall.NestedLinkElementsPostProcessorTestConfiguration;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = NestedLinkElementsPostProcessorTestConfigurationLoader.class)
public class NestedLinkElementsPostProcessorTest extends AbstractProcessorTest {

	static class NestedLinkElementsPostProcessorTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public NestedLinkElementsPostProcessorTestConfigurationLoader() {
			super(NestedLinkElementsPostProcessorTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/links/nested/";

	@Test
	@DirtiesContext
	public void testNoCacheScriptPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_nested_link_input.html", 
				HTML_FILE_DIRECTORY + "1_test_nested_link_result.html");
	}

}
