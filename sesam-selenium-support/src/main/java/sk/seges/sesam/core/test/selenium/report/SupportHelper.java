package sk.seges.sesam.core.test.selenium.report;

import sk.seges.sesam.core.test.selenium.configuration.annotation.Report;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings.SupportSettings;
import sk.seges.sesam.core.test.selenium.report.model.TestResult;

public class SupportHelper extends AbstractReportHelper  {

	protected TestResult testInfo;
	
	protected void setTestInfo(TestResult testInfo) {
		this.testInfo = testInfo;
	}
	
	protected String getOutputDirectory(SupportSettings support) {
		if (support.getDirectory().contains(Report.CURRENT_DATE) || support.getDirectory().contains(Report.CURRENT_TIME)) {
			support.setDirectory(support.getDirectory().replaceAll(Report.CURRENT_DATE, getCurrentDate()).replaceAll(Report.CURRENT_TIME, getCurrentTime()));
		}
		
		if (support.getDirectory().contains(Report.TEST_CASE_NAME) || support.getDirectory().contains(Report.TEST_NAME)) {
			support.setDirectory(support.getDirectory().replaceAll(Report.TEST_CASE_NAME, testInfo.getTestCase().getSimpleName()).replaceAll(Report.TEST_NAME, testInfo.getTestMethod()));
		}
		
		return support.getDirectory();
	}
	
	protected String getCurrentDate() {
		return getTimeStamp(DATE_FORMAT);
	}
	
	protected String getCurrentTime() {
		return getTimeStamp(TIME_FORMAT);
	}

}
