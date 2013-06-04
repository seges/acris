package sk.seges.sesam.core.test.webdriver.function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ElementVisible extends AbstractElementFunction {

	private final boolean visible;
	
	ElementVisible(By locator, boolean multiple, boolean visible) {
		super(locator, multiple);
		this.visible = visible;
	}

	ElementVisible(WebElement webElement, boolean visible) {
		super(webElement, false);
		this.visible = visible;
	}

	@Override
	protected boolean isElementSuitable(WebElement webElement) {
		return webElement.isDisplayed() == visible;
	}
}