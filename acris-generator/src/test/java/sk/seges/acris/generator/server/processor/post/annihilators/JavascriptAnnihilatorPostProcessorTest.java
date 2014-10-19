package sk.seges.acris.generator.server.processor.post.annihilators;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.spring.configuration.anihilators.JavascriptAnnihilatorTestConfiguration;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = JavascriptAnnihilatorPostProcessorTest.JavascriptAnnihilatorTestConfigurationLoader.class)
public class JavascriptAnnihilatorPostProcessorTest extends AbstractProcessorTest {

    static class JavascriptAnnihilatorTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

        public JavascriptAnnihilatorTestConfigurationLoader() {
            super(JavascriptAnnihilatorTestConfiguration.class);
        }
    }

    private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/javascript/";

    @Override
    protected GeneratorToken getDefaultToken() {
        GeneratorToken token = super.getDefaultToken();
        token.setDefaultToken(true);
        return token;
    }

    @Test
    @DirtiesContext
    public void testNoCacheScriptPostProcessor() {
        runTest(HTML_FILE_DIRECTORY + "1_javascript_input.html",
                HTML_FILE_DIRECTORY + "1_javascript_result.html");
    }
}
