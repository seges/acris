package sk.seges.sesam.core.test.selenium.report;

import sk.seges.sesam.core.test.selenium.configuration.annotation.Report;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings.SupportSettings;


public class SupportHelper<T> extends AbstractReportHelper  {

	protected T resultData;
	
	protected void setResultData(T resultData) {
		this.resultData = resultData;
	}
	
	protected String getOutputDirectory(SupportSettings support) {
		if (support.getDirectory().contains(Report.CURRENT_DATE) || support.getDirectory().contains(Report.CURRENT_TIME)) {
			support.setDirectory(support.getDirectory().replaceAll(Report.CURRENT_DATE, getCurrentDate()).replaceAll(Report.CURRENT_TIME, getCurrentTime()));
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
