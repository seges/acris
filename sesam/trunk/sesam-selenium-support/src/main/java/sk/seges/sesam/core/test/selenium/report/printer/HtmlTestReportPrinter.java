package sk.seges.sesam.core.test.selenium.report.printer;

import sk.seges.sesam.core.test.selenium.configuration.annotation.Report;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings.SupportSettings;
import sk.seges.sesam.core.test.selenium.report.model.TestCaseResult;

public class HtmlTestReportPrinter extends AbstractHtmlReportPrinter<TestCaseResult> {

	private static final String DEFAULT_TEMPLATE_FILE = Report.CLASSPATH_TEMPLATE_PREFIX + "sk/seges/sesam/selenium/report/standard/test_default.vm";

	private String testMethod;
	
	public HtmlTestReportPrinter(ReportSettings reportSettings) {
		super(reportSettings);
	}

	@Override
	protected String getDefaultTemplatePath() {
		return DEFAULT_TEMPLATE_FILE;
	}

	@Override
	protected String getTemplateRawPath(ReportSettings settings) {
		return settings.getHtml().getTestTemplatePath();
	}	

	@Override
	protected String getReportFileName(TestCaseResult testCaseResult) {
		return "index.html";
	}

	@Override
	protected String getOutputDirectory(SupportSettings support) {
		if (support.getDirectory().contains(Report.CURRENT_DATE) || support.getDirectory().contains(Report.CURRENT_TIME)) {
			support.setDirectory(support.getDirectory().replaceAll(Report.CURRENT_DATE, getCurrentDate()).replaceAll(Report.CURRENT_TIME, getCurrentTime()));
		}
		
		if (support.getDirectory().contains(Report.TEST_CASE_NAME) || support.getDirectory().contains(Report.TEST_NAME)) {
			support.setDirectory(support.getDirectory().replaceAll(Report.TEST_CASE_NAME, resultData.getTestCase().getSimpleName()).replaceAll(Report.TEST_NAME, resultData.getTestMethod()));
		}
		
		return support.getDirectory();
	}

	@Override
	public void initialize(TestCaseResult testCaseResult) {
		super.initialize(testCaseResult);
        testMethod = null;
	}
	
	@Override
	public void print(TestCaseResult testCaseResult) {
		if (!isHtmlReportEnabled()) {
			return;
		}

		if (testMethod == null) {
			testMethod = testCaseResult.getTestMethod();
			
			if (testMethod != null) {
				super.print(testCaseResult);
			}
		}
	}
}