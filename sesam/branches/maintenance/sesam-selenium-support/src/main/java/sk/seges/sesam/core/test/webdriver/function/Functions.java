package sk.seges.sesam.core.test.webdriver.function;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;

public class Functions {
	
	public static Function<WebDriver, Alert> alertPresent(String text) {
		return new AlertPresent(text);
	}
	
	public static Function<WebDriver, WebElement> elementsVisible(By locator) {
		return new ElementVisible(locator, true, true);
	}

	public static Function<WebDriver, WebElement> elementVisible(By locator) {
		return new ElementVisible(locator, false, true);
	}

	public static Function<WebDriver, WebElement> elementVisible(WebElement element) {
		return new ElementVisible(element, true);
	}

	public static Function<WebDriver, WebElement> elementNotVisible(By locator) {
		return new ElementVisible(locator, false, false);
	}

	public static Function<WebDriver, WebElement> elementNotVisible(WebElement element) {
		return new ElementVisible(element, false);
	}

	public static Function<WebDriver, WebElement> elementsEnabled(By locator) {
		return new ElementEnabled(locator, true, true);
	}

	public static Function<WebDriver, WebElement> elementEnabled(By locator) {
		return new ElementEnabled(locator, false, true);
	}

	public static Function<WebDriver, WebElement> elementDisabled(By locator) {
		return new ElementEnabled(locator, false, false);
	}
	
	public static Function<WebDriver, WebElement> elementContainsText(By locator, String text) {
		return new ElementContainsText(locator, false, text);
	}
	
	/**
	 * @return {@link ActiveRequestCondition}
	 */
	public static Function<WebDriver, Boolean> noActiveRequest() {
		return new ActiveRequestCondition();
	}
}