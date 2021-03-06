package sk.seges.acris.generator.server.processor.post.alters;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import sk.seges.acris.generator.server.processor.post.AbstractProcessorTest;
import sk.seges.acris.generator.server.processor.post.alters.NiceURLLinkPostProcessorTest.NiceURLLinkPostProcessorTestConfigurationLoader;
import sk.seges.acris.generator.server.spring.configuration.alters.NiceurlPathTestConfiguration;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.sesam.spring.ParametrizedAnnotationConfigContextLoader;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(loader = NiceURLLinkPostProcessorTestConfigurationLoader.class)
public class NiceURLLinkPostProcessorTest extends AbstractProcessorTest {

	static class NiceURLLinkPostProcessorTestConfigurationLoader extends ParametrizedAnnotationConfigContextLoader {

		public NiceURLLinkPostProcessorTestConfigurationLoader() {
			super(NiceurlPathTestConfiguration.class);
		}
	}

	private String HTML_FILE_DIRECTORY = "sk/seges/acris/generator/server/processor/post/niceurl/";

	@Test
	@DirtiesContext
	public void testNiceUrlPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "1_test_niceurl_input.html", 
				HTML_FILE_DIRECTORY + "1_test_niceurl_result.html");
	}

	@Test
	@DirtiesContext
	public void testNiceUrlHashPostProcessor() {
        webSettingsService.getWebSettings(getDefaultToken().getWebId()).setTopLevelDomain(null);
		runTest(HTML_FILE_DIRECTORY + "2_test_niceurl_hash_input.html",
                HTML_FILE_DIRECTORY + "2_test_niceurl_hash_result.html");
	}

	@Test
	@DirtiesContext
	public void testNiceUrlAbsolutePostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "3_test_niceurl_absolute_input.html", 
				HTML_FILE_DIRECTORY + "3_test_niceurl_absolute_result.html");
	}

	@Test
	@DirtiesContext
	public void testNiceUrlComplexPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "4_test_niceurl_complex_input.html", 
				HTML_FILE_DIRECTORY + "4_test_niceurl_complex_result.html");
	}
	
	@Test
	@DirtiesContext
	public void testNiceUrlDefaultPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "5_test_niceurl_default_input.html", 
				HTML_FILE_DIRECTORY + "5_test_niceurl_default_result.html", getToken());
	}
	
	@Test
	@DirtiesContext
	public void testNiceUrlMailToPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "6_test_niceurl_mailto_input.html", 
				HTML_FILE_DIRECTORY + "6_test_niceurl_mailto_result.html", getToken());
	}
	
	@Test
	@DirtiesContext
	public void testNiceUrlInvalidMailToPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "7_test_niceurl_invalid_mailto_input.html", 
				HTML_FILE_DIRECTORY + "7_test_niceurl_invalid_mailto_result.html", getToken());
	}

	@Test
	@DirtiesContext
	public void testNiceUrlJavascriptLinkPostProcessor() {
		runTest(HTML_FILE_DIRECTORY + "8_test_niceurl_javascript_input.html", 
				HTML_FILE_DIRECTORY + "8_test_niceurl_javascript_result.html", getToken());
	}

	protected GeneratorToken getToken() {
		GeneratorToken token = new GeneratorToken();
		token.setLanguage("en");
		token.setNiceUrl("home");
		token.setWebId("www.seges.sk");
		return token;
	}

}