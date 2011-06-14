package sk.seges.sesam.core.test.selenium.filter;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class FilterNotVisible implements ExpectedCondition<List<WebElement>> {

	private List<WebElement> webElements;
	
	FilterNotVisible(List<WebElement> webElements) {
		this.webElements = webElements;
	}

	FilterNotVisible(WebElement webElement) {
		webElements = new ArrayList<WebElement>();
		webElements.add(webElement);
	}

	@Override
	public List<WebElement> apply(WebDriver webDriver) {
		List<WebElement> result = new ArrayList<WebElement>();
		
		for (WebElement webElement: webElements) {
			if (webElement != null && webElement instanceof WrapsElement && ((WrapsElement) webElement).getWrappedElement().isDisplayed()) {
				result.add(((WrapsElement) webElement).getWrappedElement());
			}
			
			if (webElement.isDisplayed()) {
				result.add(webElement);
			}
		}
		return result;
	}
}