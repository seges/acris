package sk.seges.sesam.core.test.webdriver.action.logging;

import java.util.List;

import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.ClickAction;
import org.openqa.selenium.internal.Locatable;

import sk.seges.sesam.core.test.webdriver.report.ActionsListener;

public class LoggingClickAction extends ClickAction {

	private final List<? extends ActionsListener> listeners;
	private final WebDriver webDriver;
	private final WebElement webElement;
	
	public LoggingClickAction(Mouse mouse, WebElement onElement, List<? extends ActionsListener> listeners, WebDriver webDriver) {
		super(mouse, (Locatable)onElement);
		this.listeners = listeners;
		this.webDriver = webDriver;
		this.webElement = onElement;
	}

	@Override
	public void perform() {
		for (ActionsListener listener: listeners) {
			listener.beforeClickOn(webElement, webDriver);
		}
		
		super.perform();
		for (ActionsListener listener: listeners) {
			listener.afterClickOn(webElement, webDriver);
		}
	}
}