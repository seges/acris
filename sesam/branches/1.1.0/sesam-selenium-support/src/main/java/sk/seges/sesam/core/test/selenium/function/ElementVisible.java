package sk.seges.sesam.core.test.selenium.function;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class ElementVisible implements ExpectedCondition<WebElement> {

	private List<WebElement> webElements;
	
	ElementVisible(List<WebElement> webElements) {
		this.webElements = webElements;
	}

	@Override
	public WebElement apply(WebDriver webDriver) {
		for (WebElement webElement: webElements) {
			if (webElement != null && webElement instanceof WrapsElement && ((WrapsElement) webElement).getWrappedElement().isDisplayed()) {
				return ((WrapsElement) webElement).getWrappedElement();
			}
			
			if (webElement.isDisplayed()) {
				return webElement;
			}
		}
		return null;
	}
}