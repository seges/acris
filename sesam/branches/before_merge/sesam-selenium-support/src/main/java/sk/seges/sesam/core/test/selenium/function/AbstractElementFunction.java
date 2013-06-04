package sk.seges.sesam.core.test.selenium.function;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public abstract class AbstractElementFunction implements ExpectedCondition<WebElement> {

	protected final boolean multiple;
	protected By locator;
	protected WebElement element;

	protected AbstractElementFunction(By locator, boolean multiple) {
		this.multiple = multiple;
		this.locator = locator;
	}

	protected AbstractElementFunction(WebElement element, boolean multiple) {
		this.multiple = multiple;
		this.element = element;
	}

	protected List<WebElement> findWebElements(WebDriver webDriver) {
		List<WebElement> webElements = null;
		
		webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

		if (locator != null) {
			if (multiple) {
				webElements = webDriver.findElements(locator);
			} else {
				webElements = new ArrayList<WebElement>();
				webElements.add(webDriver.findElement(locator));
			}
		} else {
			webElements = new ArrayList<WebElement>();
			webElements.add(element);
		}

		webDriver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);

		return webElements;
	}
	
	@Override
	public WebElement apply(WebDriver webDriver) {
		
		for (WebElement webElement: findWebElements(webDriver)) {
			if (webElement != null && webElement instanceof WrapsElement && isElementSuitable(((WrapsElement) webElement).getWrappedElement())) {
				return ((WrapsElement) webElement).getWrappedElement();
			}
			
			if (isElementSuitable(webElement)) {
				return webElement;
			}
		}
		return null;
	}

	protected abstract boolean isElementSuitable(WebElement webElement);
}