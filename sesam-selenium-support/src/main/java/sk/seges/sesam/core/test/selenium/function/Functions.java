package sk.seges.sesam.core.test.selenium.function;

import java.util.List;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.google.common.base.Function;

public class Functions {

	public static Function<WebDriver, WebElement> elementPresent(WebElement webElement) {
		return new ElementPresent(webElement);
	}

	public static Function<WebDriver, Alert> alertPresent(String text) {
		return new AlertPresent(text);
	}
	
	public static Function<WebDriver, WebElement> elementVisible (List<WebElement> webElements) {
		return new ElementVisible (webElements);
	}
	
}