package sk.seges.sesam.core.test.webdriver.api;

import org.openqa.selenium.WebDriver;

public interface SesamWebDriver extends WebDriver {

	SesamTargetLocator switchTo();
}
