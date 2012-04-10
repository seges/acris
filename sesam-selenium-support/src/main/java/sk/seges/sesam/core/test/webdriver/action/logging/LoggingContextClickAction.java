package sk.seges.sesam.core.test.webdriver.action.logging;

import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.ContextClickAction;
import org.openqa.selenium.internal.Locatable;

import sk.seges.sesam.core.test.webdriver.report.ActionsListener;

public class LoggingContextClickAction extends ContextClickAction {

	private final ActionsListener listener;
	private final WebDriver webDriver;
	private final WebElement webElement;

	public LoggingContextClickAction(Mouse mouse, WebElement webElement, ActionsListener listener, WebDriver webDriver) {
		super(mouse, (Locatable)webElement);
		this.listener = listener;
		this.webDriver = webDriver;
		this.webElement = webElement; 
	}

	@Override
	public void perform() {
		listener.beforeContextClick(webElement, webDriver);
		super.perform();
		listener.afterContextClick(webElement, webDriver);
	}
}