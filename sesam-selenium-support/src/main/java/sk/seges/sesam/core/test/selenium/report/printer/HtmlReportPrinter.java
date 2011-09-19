package sk.seges.sesam.core.test.selenium.report.printer;

import java.io.File;

import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.report.AbstractReportHelper;
import sk.seges.sesam.core.test.selenium.report.model.TestInfo;

public class HtmlReportPrinter extends AbstractReportHelper implements ReportPrinter {

	private ReportSettings reportSettings;
	
	public HtmlReportPrinter(ReportSettings reportSettings) {
		this.reportSettings = reportSettings;
	}
	
	@Override
	public void initialize(TestInfo testInfo) {
		String fileName = testInfo.getTestCase().getSimpleName() + "_" + testInfo.getTestMethod() + "_" + getTimeStamp();
		File file = new File(getResultDirectory(), fileName);
		
		 
	}

	@Override
	public void print(TestInfo testInfo) {
		// TODO Auto-generated method stub
	}

	@Override
	public void finish(TestInfo testInfo) {
		// TODO Auto-generated method stub	
	}
}