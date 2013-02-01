package sk.seges.sesam.core.test.webdriver.function;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public class ElementEnabled extends AbstractElementFunction {

	private boolean enabled = true;
	
	ElementEnabled(By locator, boolean multiple, boolean enabled) {
		super(locator, multiple);
		this.enabled = enabled;
	}

	@Override
	protected boolean isElementSuitable(WebElement webElement) {
		return webElement.isEnabled() == enabled;
	}
}