package sk.seges.sesam.core.test.webdriver.core;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

public class DefaultNavigableElement<T extends NavigablePage> implements NavigableWebElement<T> {

	private WebElement webElement;
	private T navigablePage;
	
	public DefaultNavigableElement(WebElement webElement, T navigablePage) {
		this.webElement = webElement;
		this.navigablePage = navigablePage;
	}
	
	@Override
	public void click() {
		webElement.click();
	}

	@Override
	public void submit() {
		webElement.submit();
	}

	@Override
	public void sendKeys(CharSequence... keysToSend) {
		webElement.sendKeys(keysToSend);
	}

	@Override
	public void clear() {
		webElement.clear();
	}

	@Override
	public String getTagName() {
		return webElement.getTagName();
	}

	@Override
	public String getAttribute(String name) {
		return webElement.getAttribute(name);
	}

	@Override
	public boolean isSelected() {
		return webElement.isSelected();
	}

	@Override
	public boolean isEnabled() {
		return webElement.isEnabled();
	}

	@Override
	public String getText() {
		return webElement.getText();
	}

	@Override
	public List<WebElement> findElements(By by) {
		return webElement.findElements(by);
	}

	@Override
	public WebElement findElement(By by) {
		return webElement.findElement(by);
	}

	@Override
	public void navigate() {
		webElement.click();
	}

	@Override
	public WebElement getWebElement() {
		return webElement;
	}

	@Override
	public T getNavigablePage() {
		return navigablePage;
	}

	@Override
	public boolean isDisplayed() {
		return webElement.isDisplayed();
	}

	@Override
	public Point getLocation() {
		return webElement.getLocation();
	}

	@Override
	public Dimension getSize() {
		return webElement.getSize();
	}

	@Override
	public String getCssValue(String propertyName) {
		return webElement.getCssValue(propertyName);
	}
}