package sk.seges.sesam.core.test.selenium.report.support;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;

import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings;
import sk.seges.sesam.core.test.selenium.report.ScreenshotsWebDriverEventListener;
import sk.seges.sesam.core.test.selenium.report.SupportHelper;

public class ScreenshotSupport extends SupportHelper implements ReportSupport {

	private final ReportSettings reportSettings;
	private final WebDriver webDriver;
	private final ScreenshotsWebDriverEventListener screenshotsWebDriverEventListener;
	
	public ScreenshotSupport(WebDriver webDriver, ReportSettings reportSettings) {
		this.reportSettings = reportSettings;
		this.webDriver = webDriver;
		this.screenshotsWebDriverEventListener = new ScreenshotsWebDriverEventListener(this, reportSettings);
	}
	
	public EventFiringWebDriver registerTo(EventFiringWebDriver eventFiringWebDriver) {
		eventFiringWebDriver.register(screenshotsWebDriverEventListener);
		return eventFiringWebDriver;
	}

	public void initialize() {
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
	
	public void makeScreenshot(String name) {
		try {
			if (!initialized) {
				if (!new File(getScreenshotDirectory()).exists()) {
					new File(getScreenshotDirectory()).mkdirs();
				}
				initialized = true;
			}
			File screnshotFile = ((TakesScreenshot)webDriver).getScreenshotAs(OutputType.FILE);
			FileUtils.copyFile(screnshotFile, new File(getScreenshotDirectory() + File.separator + name + ".png"));
		} catch (Exception e) {}
	}
	
	public void makeScreenshot() {
		makeScreenshot("screenshot_" + getTimeStamp());
	}
	
	@Override
	public void finish() {
		makeScreenshot();
	}
}