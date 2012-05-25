package sk.seges.acris.generator.server.processor.post;

import java.io.IOException;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;

import sk.seges.acris.core.server.utils.io.StringFile;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.HtmlPostProcessor;
import sk.seges.acris.generator.server.processor.factory.HtmlProcessorFactory;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.site.server.service.IWebSettingsServiceLocal;
import sk.seges.acris.site.shared.service.IWebSettingsService;

public abstract class AbstractProcessorTest {

	protected String inputHtmlFileName;
	protected String resultHtmlFileName;
	
	@Autowired
	protected HtmlProcessorFactory htmlProcessorFactory;

	@Autowired
	protected ContentDataProvider contentInfoProvider;

	@Autowired
	protected IWebSettingsServiceLocal webSettingsService;

	protected String getInputHtmlFileName() {
		return inputHtmlFileName;
	}

	protected String getResultHtmlFileName() {
		return resultHtmlFileName;
	}

	protected void runTest(String inputHtmlFileName, String resultHtmlFileName, GeneratorToken token) {
		runTest(inputHtmlFileName, resultHtmlFileName, token, false);
	}
	
	protected void runTest(String inputHtmlFileName, String resultHtmlFileName, GeneratorToken token, boolean indexFile) {
		this.inputHtmlFileName = inputHtmlFileName;
		this.resultHtmlFileName = resultHtmlFileName;
		
		HtmlPostProcessor htmlPostProcessing = htmlProcessorFactory.create(webSettingsService.getWebSettings(token.getWebId()));
		
		String html = htmlPostProcessing.getProcessedContent(getInputHtml(), token, indexFile);
		if (html != null) {
			Assert.assertTrue("Result HTML is not equals to the expected result. Expected result: " + getResultHtml() + ". Current result: " + html, compare(html));
		} else {
			Assert.assertFalse("Processing/confguration failure occured.", true);
		}
	}

	protected void runTest(String inputHtmlFileName, String resultHtmlFileName) {
		runTest(inputHtmlFileName, resultHtmlFileName, getDefaultToken());
	}
	
	protected GeneratorToken getDefaultToken() {
		GeneratorToken token = new GeneratorToken();
		token.setLanguage("en");
		token.setNiceUrl("test");
		token.setWebId("www.seges.sk");
		return token;
	}

	protected boolean compare(String result) {
		return getResultHtml().equals(result);
	}

	protected String getInputHtml() {
		return getHtml(getInputHtmlFileName());
	}

	private String getHtml(String fileName) {

		final StringFile file = StringFile.getFileDescriptor(fileName);

		try {
			return file.readTextFromFile();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	protected String getResultHtml() {
		return getHtml(getResultHtmlFileName());
	}
}