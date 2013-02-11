/**
 * 
 */
package sk.seges.sesam.core.test.webdriver.function;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

/**
 * Compares number of active RPC requests, if zero, returns true.
 * 
 * It is connected e.g. with ActiveRequestAsyncCallback that tracks running GWT-RPC calls.
 * 
 * @author ladislav.gazo
 */
public class ActiveRequestCondition implements ExpectedCondition<Boolean> {
	@Override
	public Boolean apply(WebDriver input) {
		WebElement element = input.findElement(By.id("activeRequest"));
		final String outstanding;
		if(element.isDisplayed()) {
			outstanding = element.getText();
		} else {
			// this is actually the only way because Selenium has a feature
			// where getText on not visible elements returns empty string.
			outstanding = (String) ((JavascriptExecutor) input).executeScript(
					"return arguments[0].innerHTML;", element);
			outstanding.trim();
		}
		return "0".equals(outstanding);
	}
}
