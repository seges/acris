package sk.seges.sesam.core.test.webdriver.action.logging;

import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.ClickAction;
import org.openqa.selenium.internal.Locatable;

import sk.seges.sesam.core.test.webdriver.report.ActionsListener;

public class LoggingClickAction extends ClickAction {

	private final ActionsListener listener;
	private final WebDriver webDriver;
	private final WebElement webElement;
	
	public LoggingClickAction(Mouse mouse, WebElement onElement, ActionsListener listener, WebDriver webDriver) {
		super(mouse, (Locatable)onElement);
		this.listener = listener;
		this.webDriver = webDriver;
		this.webElement = onElement;
	}

	@Override
	public void perform() {
		listener.beforeClickAndHold(webElement, webDriver);
		super.perform();
		listener.afterClickAndHold(webElement, webDriver);
	}
}