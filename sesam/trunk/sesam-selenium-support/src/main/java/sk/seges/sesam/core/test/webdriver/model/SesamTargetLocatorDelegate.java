package sk.seges.sesam.core.test.webdriver.model;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;

import sk.seges.sesam.core.test.webdriver.api.SesamTargetLocator;

class SesamTargetLocatorDelegate implements SesamTargetLocator {

	private final TargetLocator targetLocator;
	
	private WebElement frame;
	private Integer frameIndex;
	private String frameName;
	
	public SesamTargetLocatorDelegate(TargetLocator targetLocator) {
		this.targetLocator = targetLocator;
	}
	
	@Override
	public WebElement activeElement() {
		return targetLocator.activeElement();
	}

	@Override
	public Alert alert() {
		return targetLocator.alert();
	}

	@Override
	public WebDriver defaultContent() {
		return targetLocator.defaultContent();
	}

	@Override
	public WebDriver frame(int arg0) {
		this.frameIndex = arg0;
		this.frame = null;
		this.frameName = null;
		return targetLocator.frame(arg0);
	}

	@Override
	public WebDriver frame(String arg0) {
		this.frameName = arg0;
		this.frameIndex = null;
		this.frame = null;
		return targetLocator.frame(arg0);
	}

	@Override
	public WebDriver frame(WebElement arg0) {
		this.frameIndex = null;
		this.frameName = null;
		this.frame = arg0;
		return targetLocator.frame(arg0);
	}

	@Override
	public WebDriver window(String arg0) {
		return targetLocator.window(arg0);
	}

	@Override
	public void clearFrame() {
		this.frameIndex = null;
		this.frameName = null;
		this.frame = null;
	}
	
	@Override
	public void restoreFrame() {
		if (frame != null) {
			frame(frame);
		} else if (frameIndex != null) {
			frame(frameIndex);
		} else if (frameName != null) {
			frame(frameName);
		}
	}
}