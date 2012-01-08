package sk.seges.sesam.core.test.selenium.runner;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.model.CoreSeleniumSettingsProvider;
import sk.seges.sesam.core.test.selenium.report.model.TestResult;
import sk.seges.sesam.core.test.selenium.report.printer.HtmlReportPrinter;

public class SeleniumSuiteRunner {

	private Map<String, HtmlReportPrinter> htmlPrinters = new HashMap<String, HtmlReportPrinter>();
	private Map<String, TestResult> testResults = new HashMap<String, TestResult>();
	
	protected SeleniumSuiteRunner() {
	}

	private interface ElementFactory<T> {
		T construct(CoreSeleniumSettingsProvider settingsProvider);
	}
	
	protected void printReports() {
		for (Entry<String, HtmlReportPrinter> printers: htmlPrinters.entrySet()) {
			TestResult testResult = testResults.get(printers.getKey());
			HtmlReportPrinter printer = printers.getValue();
			
			printer.print(testResult);
			printer.finish(testResult);
		}
	}
	
	protected TestResult getTestResult(AbstractSeleniumTest testCase) {
		return get(testCase, testResults, new ElementFactory<TestResult>() {

			@Override
			public TestResult construct(CoreSeleniumSettingsProvider settingsProvider) {
				return new TestResult();
			}
		});
	}

	protected HtmlReportPrinter getPrinter(AbstractSeleniumTest testCase) {
		return get(testCase, htmlPrinters, new ElementFactory<HtmlReportPrinter>() {

			@Override
			public HtmlReportPrinter construct(CoreSeleniumSettingsProvider settingsProvider) {
				return new HtmlReportPrinter(settingsProvider.getReportSettings());
			}
		});
	}

	protected void handleTestResult() {
		int failures = 0;
		for (Entry<String, TestResult> entrySets: testResults.entrySet()) {
			failures += entrySets.getValue().getFailedTestCasesCount();
		}

		if (failures > 0) {
			throw new RuntimeException(failures + " tests are failing!");
		}
	}
	
	protected <T> T get(AbstractSeleniumTest testCase, Map<String, T> elements, ElementFactory<T> factory) {
		CoreSeleniumSettingsProvider settingsProvider = testCase.ensureSettings();
		
		String templatePath = getTemplatePath(settingsProvider);
		
		if (templatePath == null) {
			return null;
		}
		
		T result = elements.get(templatePath);
		
		if (result == null) {
			result = factory.construct(settingsProvider);
			//new HtmlReportPrinter(settingsProvider.getReportSettings());
			elements.put(templatePath, result);
		}
		
		return result;
	}
	
	private String getTemplatePath(CoreSeleniumSettingsProvider settings) {
		if (settings == null || settings.getReportSettings() == null || settings.getReportSettings().getHtml() == null) {
			return null;
		}
		
		return settings.getReportSettings().getHtml().getSuiteTemplatePath();
	}
	
    public void run(AbstractSeleniumTest test) {
    	test.setUp();
       	test.runTests();
        test.tearDown();
    }
}