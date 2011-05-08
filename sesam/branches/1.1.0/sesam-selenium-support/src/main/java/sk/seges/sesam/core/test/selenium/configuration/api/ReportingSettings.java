package sk.seges.sesam.core.test.selenium.configuration.api;

public interface ReportingSettings {
	
	boolean isProduceScreenshots();
	
	String getResultDirectory();
	
	String getScreenshotsDirectory();
}