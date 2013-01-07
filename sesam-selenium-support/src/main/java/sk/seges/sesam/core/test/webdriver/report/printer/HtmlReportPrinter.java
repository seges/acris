package sk.seges.sesam.core.test.webdriver.report.printer;

import sk.seges.sesam.core.test.selenium.configuration.annotation.Report;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings.SupportSettings;
import sk.seges.sesam.core.test.webdriver.report.model.TestResult;

public class HtmlReportPrinter extends AbstractHtmlReportPrinter<TestResult> {

	private static final String DEFAULT_TEMPLATE_FILE = Report.CLASSPATH_TEMPLATE_PREFIX + "sk/seges/sesam/selenium/report/standard/suite_default.vm";

	public HtmlReportPrinter(ReportSettings reportSettings) {
		super(reportSettings);
	}

	@Override
	protected String getDefaultTemplatePath() {
		return DEFAULT_TEMPLATE_FILE;
	}

	protected String getOutputDirectory(SupportSettings support) {
		return "";
	}
	
	@Override
	protected String getTemplateRawPath(ReportSettings settings) {
		return settings.getHtml().getSuiteTemplatePath();
	}

	@Override
	protected String getReportFileName(TestResult resultData) {
		return "result_" + getTimeStamp() + ".html";
	}

	@Override
	protected void postProcessResultData() {
	}	
}