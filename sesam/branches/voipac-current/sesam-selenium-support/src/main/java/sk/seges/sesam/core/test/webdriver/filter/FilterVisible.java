package sk.seges.sesam.core.test.webdriver.filter;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class FilterVisible implements ExpectedCondition<List<WebElement>> {

	private List<WebElement> webElements;
	private boolean visible;
	
	FilterVisible(List<WebElement> webElements, boolean visible) {
		this.webElements = webElements;
		this.visible = visible;
	}

	FilterVisible(WebElement webElement, boolean visible) {
		webElements = new ArrayList<WebElement>();
		webElements.add(webElement);
		this.visible = visible;
	}

	@Override
	public List<WebElement> apply(WebDriver webDriver) {
		List<WebElement> result = new ArrayList<WebElement>();
		
		for (WebElement webElement: webElements) {
			if (webElement != null && webElement instanceof WrapsElement && ((WrapsElement) webElement).getWrappedElement().isDisplayed() != visible) {
				result.add(((WrapsElement) webElement).getWrappedElement());
			}
			
			if (webElement.isDisplayed() != visible) {
				result.add(webElement);
			}
		}
		return result;
	}
}