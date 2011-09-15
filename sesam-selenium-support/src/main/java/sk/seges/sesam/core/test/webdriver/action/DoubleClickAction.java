package sk.seges.sesam.core.test.webdriver.action;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;

import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSettings;
import sk.seges.sesam.core.test.selenium.configuration.api.Browsers;

public class DoubleClickAction implements Action {

	private WebDriver webDriver;
	private SeleniumSettings testEnvironment;
	private WebElement webElement;
	
	public DoubleClickAction(WebDriver webDriver, SeleniumSettings testEnvironment, WebElement webElement) {
		this.webDriver = webDriver;
		this.testEnvironment = testEnvironment;
		this.webElement = webElement;
	}

	@Override
	public void perform() {
		if (testEnvironment.getBrowser().equals(Browsers.FIREFOX.toString())) {
			((JavascriptExecutor) webDriver).executeScript("var evt = document.createEvent('MouseEvents'); evt.initMouseEvent('dblclick',true, true, window, 0, 0, 0, 0, 0, false, false, false, false, 0,null); arguments[0].dispatchEvent(evt);", webElement);
		} else if (testEnvironment.getBrowser().equals(Browsers.IE.toString())) {
			((JavascriptExecutor) webDriver).executeScript("arguments[0].fireEvent('ondblclick');", webElement); 
		} else {
			throw new RuntimeException("Not supported browser for double click action");
		}
	}
}