package sk.seges.sesam.core.test.selenium.function;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class ElementVisible implements ExpectedCondition<WebElement> {

	private List<WebElement> webElements;
	private boolean visible = true;
	
	ElementVisible(List<WebElement> webElements, boolean visible) {
		this.webElements = webElements;
		this.visible = visible;
	}

	ElementVisible(WebElement webElement, boolean visible) {
		webElements = new ArrayList<WebElement>();
		webElements.add(webElement);
		this.visible = visible;
	}

	@Override
	public WebElement apply(WebDriver webDriver) {
		for (WebElement webElement: webElements) {
			if (webElement != null && webElement instanceof WrapsElement && ((WrapsElement) webElement).getWrappedElement().isDisplayed() == visible) {
				return ((WrapsElement) webElement).getWrappedElement();
			}
			
			if (webElement.isDisplayed() == visible) {
				return webElement;
			}
		}
		return null;
	}
}