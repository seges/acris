package sk.seges.sesam.core.test.webdriver.model;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.HasInputDevices;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import sk.seges.sesam.core.test.webdriver.api.SesamTargetLocator;
import sk.seges.sesam.core.test.webdriver.api.SesamWebDriver;

public class SesamWebDriverDelegate implements SesamWebDriver, HasInputDevices, JavascriptExecutor {

	private final WebDriver webDriver;
	private SesamTargetLocatorDelegate targetLocatorDelegate;
	
	public SesamWebDriverDelegate(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	@Override
	public void close() {
		webDriver.close();
	}

	@Override
	public WebElement findElement(By arg0) {
		return webDriver.findElement(arg0);
	}

	@Override
	public List<WebElement> findElements(By arg0) {
		return webDriver.findElements(arg0);
	}

	@Override
	public void get(String arg0) {
		webDriver.get(arg0);
	}

	@Override
	public String getCurrentUrl() {
		return webDriver.getCurrentUrl();
	}

	@Override
	public String getPageSource() {
		return webDriver.getPageSource();
	}

	@Override
	public String getTitle() {
		return webDriver.getTitle();
	}

	@Override
	public String getWindowHandle() {
		return webDriver.getWindowHandle();
	}

	@Override
	public Set<String> getWindowHandles() {
		return webDriver.getWindowHandles();
	}

	@Override
	public Options manage() {
		return webDriver.manage();
	}

	@Override
	public Navigation navigate() {
		return webDriver.navigate();
	}

	@Override
	public void quit() {
		webDriver.quit();
	}
	
	@Override
	public SesamTargetLocator switchTo() {
		if (targetLocatorDelegate == null) {
			targetLocatorDelegate = new SesamTargetLocatorDelegate(webDriver.switchTo());
		}
		
		return targetLocatorDelegate;
	}

	@Override
	public Keyboard getKeyboard() {
		return ((HasInputDevices)webDriver).getKeyboard();
	}

	@Override
	public Mouse getMouse() {
		return ((HasInputDevices)webDriver).getMouse();
	}

	@Override
	public Object executeAsyncScript(String arg0, Object... arg1) {
		return ((JavascriptExecutor)webDriver).executeAsyncScript(arg0, arg1);
	}

	@Override
	public Object executeScript(String arg0, Object... arg1) {
		return ((JavascriptExecutor)webDriver).executeScript(arg0, arg1);
	}
}