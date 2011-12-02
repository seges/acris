package sk.seges.sesam.core.test.selenium.report.printer;

import sk.seges.sesam.core.test.selenium.report.model.TestResult;

public interface ReportPrinter {

	void initialize(TestResult testInfo);
	
	void print(TestResult testInfo);
	
	void finish(TestResult testInfo);
}