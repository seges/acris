package sk.seges.acris.generator.server.processor.post;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import sk.seges.acris.core.server.utils.io.StringFile;
import sk.seges.acris.generator.server.processor.ContentDataProvider;
import sk.seges.acris.generator.server.processor.HtmlPostProcessor;
import sk.seges.acris.generator.server.processor.factory.HtmlProcessorFactory;
import sk.seges.acris.generator.shared.domain.GeneratorToken;
import sk.seges.acris.site.shared.service.IWebSettingsLocalService;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public abstract class AbstractProcessorTest {

	protected String inputHtmlFileName;
	protected String resultHtmlFileName;
	
	@Autowired
	protected HtmlProcessorFactory htmlProcessorFactory;

	@Autowired
	protected ContentDataProvider contentInfoProvider;

	@Autowired
	protected IWebSettingsLocalService webSettingsService;

	protected String getInputHtmlFileName() {
		return inputHtmlFileName;
	}

	protected String getResultHtmlFileName() {
		return resultHtmlFileName;
	}

    protected void runTest(String inputHtmlFileName, String resultHtmlFileName, GeneratorToken token) {
        runTest(inputHtmlFileName, resultHtmlFileName, token, false, false);
    }

    protected void runTest(String inputHtmlFileName, String resultHtmlFileName, GeneratorToken token, boolean ignoreCase) {
		runTest(inputHtmlFileName, resultHtmlFileName, token, false, ignoreCase);
	}
	
	protected void runTest(String inputHtmlFileName, String resultHtmlFileName, GeneratorToken token, boolean indexFile, boolean ignoreCase) {
		this.inputHtmlFileName = inputHtmlFileName;
		this.resultHtmlFileName = resultHtmlFileName;
		
		HtmlPostProcessor htmlPostProcessing = htmlProcessorFactory.create(
                webSettingsService.getWebSettings(token.getWebId()));
		
		String html = htmlPostProcessing.getProcessedContent(getInputHtml(), token, getDefaultToken(), indexFile, getDefaultLocale());
		if (html != null) {

            String expectedResult = getResultHtml();

            if (ignoreCase) {
                expectedResult = expectedResult.replaceAll(System.getProperty("line.separator"), "");
                expectedResult = expectedResult.replaceAll("\\t", "");
                html = html.replaceAll(System.getProperty("line.separator"), "");
                html = html.replaceAll("\\t", "");
            }

            int diff = getDiffPosition(expectedResult, html);

            if (diff > 0) {
                System.out.println("Expected char: " + expectedResult.charAt(diff));
                System.out.println("Current char: " + html.charAt(diff));
            }

			Assert.assertTrue("Result HTML is not equals to the expected result.\n\n Expected result: \n" + expectedResult + ".\n\n Current result: \n" + html +
                    ".\n\nDIFF:\nExpected result:\n" + subString(expectedResult, diff) + "\nCurrent Result:\n" + subString(html, diff) + "\n\n on position: " + diff + "\n",
                    compare(expectedResult, html));
		} else {
			Assert.assertFalse("Processing/configuration failure occurred.", true);
		}
	}

    protected String subString(String text, int position) {
        if (position == -1) {
            return null;
        }
        return text.substring(position, Math.min(position + 60, text.length() - 1));
    }

	protected void runTest(String inputHtmlFileName, String resultHtmlFileName) {
        runTest(inputHtmlFileName, resultHtmlFileName, getDefaultToken(), false);
    }

    protected void runTest(String inputHtmlFileName, String resultHtmlFileName, boolean ignoreCase) {
        runTest(inputHtmlFileName, resultHtmlFileName, getDefaultToken(), ignoreCase);
	}
	
	protected String getDefaultLocale() {
		return "en";
	}
	
	protected GeneratorToken getDefaultToken() {
		GeneratorToken token = new GeneratorToken();
		token.setLanguage("en");
		token.setNiceUrl("test");
		token.setWebId("www.seges.sk");
		return token;
	}

    protected int getDiffPosition(String expectedResult, String currentResult) {
        if (expectedResult == null || currentResult == null) {
            return -1;
        }

        for (int i = 0; i < expectedResult.length() || i < currentResult.length(); i++) {
            if (expectedResult.charAt(i) != currentResult.charAt(i)) {
                return i;
            }
        }

        return -1;
    }

    protected boolean compare(String expectedResult, String currentResult) {
		return expectedResult.equals(currentResult);
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