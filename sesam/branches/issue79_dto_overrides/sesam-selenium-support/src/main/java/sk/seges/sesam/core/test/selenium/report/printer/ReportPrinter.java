package sk.seges.sesam.core.test.selenium.report.printer;


public interface ReportPrinter<T> {

	void initialize(T testInfo);
	
	void print(T testInfo);
	
	void finish(T testInfo);
}