package sk.seges.sesam.core.test.selenium.function;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class ElementPresent implements ExpectedCondition<WebElement> {

	private WebElement webElement;

	ElementPresent(WebElement webElement) {
		this.webElement = webElement;
	}

	@Override
	public WebElement apply(WebDriver webDriver) {
		if (webElement != null && webElement instanceof WrapsElement) {
			return ((WrapsElement) webElement).getWrappedElement();
		}
		return webElement;
	}
}