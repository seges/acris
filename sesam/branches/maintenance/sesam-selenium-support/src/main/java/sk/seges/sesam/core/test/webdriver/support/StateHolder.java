package sk.seges.sesam.core.test.webdriver.support;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class StateHolder {
	
	private final By selector;
	private final WebDriver webDriver;

	private List<WebElement> webElements;
	
	public StateHolder(WebDriver webDriver, By selector) {
		this.selector = selector;
		this.webDriver = webDriver;
	}

	public StateHolder rememberState() {
		webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		webElements = webDriver.findElements(selector);
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return this;
	}

	public WebElement getLastElement() {
		webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		int size = webElements.size();
		webElements = webDriver.findElements(selector);
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		
		if (webElements.size() <= size) {
			return null;
		}
		
		return webElements.get(size);
	}
}
