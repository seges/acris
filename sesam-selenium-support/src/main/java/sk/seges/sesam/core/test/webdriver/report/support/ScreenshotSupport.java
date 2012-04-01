package sk.seges.sesam.core.test.webdriver.report.support;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.webdriver.model.EnvironmentInfo;
import sk.seges.sesam.core.test.webdriver.report.SupportHelper;
import sk.seges.sesam.core.test.webdriver.report.model.TestCaseResult;

public class ScreenshotSupport extends SupportHelper<TestCaseResult> {

	private final ReportSettings reportSettings;
	private final WebDriver webDriver;
	
	public ScreenshotSupport(WebDriver webDriver, ReportSettings reportSettings, EnvironmentInfo environmentInfo) {
		this.reportSettings = reportSettings;
		this.webDriver = webDriver;
	}
	
	private String getScreenshotDirectory() {
		if (reportSettings.getScreenshot().getSupport().getEnabled() == true && reportSettings.getScreenshot().getSupport().getDirectory() != null) {
			return getResultDirectory() + File.separator + getOutputDirectory(reportSettings.getScreenshot().getSupport());
		}

		if (reportSettings.getHtml().getSupport().getEnabled() == true && reportSettings.getHtml().getSupport().getDirectory() != null) {
			return getResultDirectory() + File.separator + getOutputDirectory(reportSettings.getHtml().getSupport());
		}
		
		return getResultDirectory();
	}

	private boolean initialized = false;
	
	public static final String DEFAULT_SCREENSHOT_EXTENSION = ".png";
	
	public void makeScreenshot(String name) {
		try {
			if (!initialized) {
				if (!new File(getScreenshotDirectory()).exists()) {
					new File(getScreenshotDirectory()).mkdirs();
				}
				initialized = true;
			}
			File screnshotFile = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screnshotFile, new File(getScreenshotDirectory() + File.separator + name + DEFAULT_SCREENSHOT_EXTENSION));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void makeScreenshot() {
		makeScreenshot("screenshot_" + getTimeStamp());
	}
}