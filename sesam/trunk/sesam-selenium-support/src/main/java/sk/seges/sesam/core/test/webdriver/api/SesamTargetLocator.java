package sk.seges.sesam.core.test.webdriver.api;

import org.openqa.selenium.WebDriver.TargetLocator;

public interface SesamTargetLocator extends TargetLocator {

	void clearFrame();
	
	void restoreFrame();
}