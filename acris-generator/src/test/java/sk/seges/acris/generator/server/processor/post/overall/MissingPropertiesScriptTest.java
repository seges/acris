package sk.seges.acris.generator.server.processor.post.overall;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.spring.configuration.overall.MissingPropertiesScriptTestConfiguration;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = MissingPropertiesScriptTest.MissingPropertiesScriptTestConfigurationLoader.class)
public class MissingPropertiesScriptTest extends AbstractProcessorTest {

	static class MissingPropertiesScriptTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public MissingPropertiesScriptTestConfigurationLoader() {
			super(MissingPropertiesScriptTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/overall/";

	@Override
	protected GeneratorToken getDefaultToken() {
		GeneratorToken defaultToken = super.getDefaultToken();
		defaultToken.setNiceUrl("");
		defaultToken.setDefaultToken(true);
		return defaultToken;
	}

	@Test
	@DirtiesContext
	public void missingPropertiesJsProcessor() {
		runTest(HTML_FILE_DIRECTORY + "missing_properties_js_input.html",
				HTML_FILE_DIRECTORY + "missing_properties_js_result.html");
	}
}
