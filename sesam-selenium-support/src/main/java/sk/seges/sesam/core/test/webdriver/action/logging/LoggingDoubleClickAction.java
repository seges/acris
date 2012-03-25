package sk.seges.sesam.core.test.webdriver.action.logging;

import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.DoubleClickAction;
import org.openqa.selenium.internal.Locatable;

import sk.seges.sesam.core.test.webdriver.report.ActionsListener;

public class LoggingDoubleClickAction extends DoubleClickAction {

	private final ActionsListener listener;
	private final WebDriver webDriver; 
	private final WebElement webElement;
	
	public LoggingDoubleClickAction(Mouse mouse, WebElement webElement, ActionsListener listener, WebDriver webDriver) {
		super(mouse, (Locatable)webElement);
		this.listener = listener;
		this.webElement = webElement;
		this.webDriver = webDriver;
	}

	@Override
	public void perform() {
		listener.beforeDoubleClickOn(webElement, webDriver);
		super.perform();
		listener.afterDoubleClickOn(webElement, webDriver);
	}
}