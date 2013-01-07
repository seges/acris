package sk.seges.sesam.core.test.webdriver.support;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;

public class StateHolder {
	
	private final By selector;
	private final WebDriver webDriver;

	private List<WebElement> webElements;
	private final Wait<WebDriver> wait;
	
	public StateHolder(Wait<WebDriver> wait, WebDriver webDriver, By selector) {
		this.selector = selector;
		this.webDriver = webDriver;
		this.wait = wait;
	}

	public StateHolder rememberState() {
		webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		webElements = webDriver.findElements(selector);
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		return this;
	}

	public WebElement getLastElement() {
		if (webElements.size() == 0) {
			return null;
		}
		return webElements.get(webElements.size() - 1);
	}

	private void refind() {
		webDriver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		webElements = webDriver.findElements(selector);
		webDriver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}
	
	public void waitUntilDeleted() {
		System.out.println("----");
		final int size = webElements.size();

		System.out.println(size);
		
		wait.until(new Function<WebDriver, Boolean>() {

			@Override
			public Boolean apply(WebDriver arg0) {
				refind();
				System.out.println(webElements.size());
				return webElements.size() < size;
			}
		});

		System.out.println("----");
	}
	
	public WebElement getNewElement() {
		final int size = webElements.size();

		System.out.println("----");
		System.out.println(size);

		wait.until(new Function<WebDriver, Boolean>() {

			@Override
			public Boolean apply(WebDriver arg0) {
				refind();
				System.out.println(webElements.size());
				return webElements.size() > size;
			}
		});
		
		System.out.println("----");

		return webElements.get(size);
	}
}
