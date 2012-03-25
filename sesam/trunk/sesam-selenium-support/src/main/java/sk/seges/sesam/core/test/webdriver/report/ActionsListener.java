package sk.seges.sesam.core.test.webdriver.report;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public interface ActionsListener {

	void beforeClickAndHold(WebElement element, WebDriver driver);
	void afterClickAndHold(WebElement element, WebDriver driver);

	void beforeDoubleClickOn(WebElement element, WebDriver driver);
	void afterDoubleClickOn(WebElement element, WebDriver driver);

	void beforeKeyDown(Keys key, WebDriver driver);
	void afterKeyDown(Keys key, WebDriver driver);

	void beforeKeyUp(Keys key, WebDriver driver);
	void afterKeyUp(Keys key, WebDriver driver);

	void beforeSendKeys(CharSequence[] keysToSend, WebDriver driver);
	void afterSendKeys(CharSequence[] keysToSend, WebDriver driver);

	void beforeButtonRelease(WebElement webElement, WebDriver driver);
	void afterButtonRelease(WebElement webElement, WebDriver driver);

	void beforeMouseMove(WebElement element, WebDriver driver);
	void afterMouseMove(WebElement element, WebDriver driver);

	void beforeMoveToOffset(WebElement element, int x, int y, WebDriver driver);
	void afterMoveToOffset(WebElement element, int x, int y, WebDriver driver);

	void beforeContextClick(WebElement element, WebDriver driver);
	void afterContextClick(WebElement element, WebDriver driver);
}