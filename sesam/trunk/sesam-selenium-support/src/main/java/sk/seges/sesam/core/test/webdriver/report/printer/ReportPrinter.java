package sk.seges.sesam.core.test.webdriver.report.printer;


public interface ReportPrinter<T> {

	void initialize(T testInfo);
	
	void print(T testInfo);
	
	void finish(T testInfo);
}