package sk.seges.sesam.core.test.selenium.report.printer;

import sk.seges.sesam.core.test.selenium.report.model.TestInfo;

public interface ReportPrinter {

	void initialize(TestInfo testInfo);
	
	void print(TestInfo testInfo);
	
	void finish(TestInfo testInfo);
}