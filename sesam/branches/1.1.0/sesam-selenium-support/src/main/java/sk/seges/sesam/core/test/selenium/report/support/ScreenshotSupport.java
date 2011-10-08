package sk.seges.sesam.core.test.selenium.report.support;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.report.AbstractReportHelper;

public class ScreenshotSupport extends AbstractReportHelper implements ReportSupport {

	private final ReportSettings reportSettings;
	private final WebDriver webDriver;
	
	public ScreenshotSupport(WebDriver webDriver, ReportSettings reportSettings) {
		this.reportSettings = reportSettings;
		this.webDriver = webDriver;
	}
	
	public void initialize() {
		if (!new File(getScreenshotDirectory()).exists()) {
			new File(getScreenshotDirectory()).mkdirs();
		}
	}
	
	private String getScreenshotDirectory() {
		if (reportSettings.getScreenshot().getDirectory() == null) {
			return getResultDirectory();
		}
		return getResultDirectory() + reportSettings.getScreenshot().getDirectory();
	}

	public void makeScreenshot() {
		try {
			File screnshotFile = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screnshotFile, new File(getScreenshotDirectory() + File.separator + "screenshot_" + getTimeStamp() + ".png"));
		} catch (Exception e) {}
	}
	
	@Override
	public void finish() {
		makeScreenshot();
	}
}