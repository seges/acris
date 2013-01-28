package sk.seges.sesam.core.test.webdriver.action.logging;

import java.util.List;

import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.ContextClickAction;
import org.openqa.selenium.internal.Locatable;

import sk.seges.sesam.core.test.webdriver.report.ActionsListener;

public class LoggingContextClickAction extends ContextClickAction {

	private final List<? extends ActionsListener> listeners;
	private final WebDriver webDriver;
	private final WebElement webElement;

	public LoggingContextClickAction(Mouse mouse, WebElement webElement, List<? extends ActionsListener> listeners, WebDriver webDriver) {
		super(mouse, (Locatable)webElement);
		this.listeners = listeners;
		this.webDriver = webDriver;
		this.webElement = webElement; 
	}

	@Override
	public void perform() {
		for (ActionsListener listener: listeners) {
			listener.beforeContextClick(webElement, webDriver);
		}
		
		super.perform();
		
		for (ActionsListener listener: listeners) {
			listener.afterContextClick(webElement, webDriver);
		}
	}
}