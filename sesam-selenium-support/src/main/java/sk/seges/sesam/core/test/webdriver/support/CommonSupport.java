package sk.seges.sesam.core.test.webdriver.support;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

public class CommonSupport {

	private static final String VALUE_ATTRIBUTE = "value";

	public WebElement clear(WebElement webElement) {
		webElement.clear();
		webElement.sendKeys(Keys.chord(Keys.CONTROL, "a"), Keys.BACK_SPACE);
		return webElement;
	}

	public String getValue(WebElement element) {
		return element.getAttribute(VALUE_ATTRIBUTE);
	}
}
