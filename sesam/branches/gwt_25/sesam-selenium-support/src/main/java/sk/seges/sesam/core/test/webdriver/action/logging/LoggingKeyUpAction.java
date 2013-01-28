package sk.seges.sesam.core.test.webdriver.action.logging;

import java.util.List;

import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.KeyUpAction;
import org.openqa.selenium.internal.Locatable;

import sk.seges.sesam.core.test.webdriver.report.ActionsListener;

public class LoggingKeyUpAction extends KeyUpAction {

	private final List<? extends ActionsListener> listeners;
	private final WebDriver webDriver;
	
	public LoggingKeyUpAction(Keyboard keyboard, Mouse mouse, WebElement element, Keys key, List<? extends ActionsListener> listeners, WebDriver webDriver) {
		super(keyboard, mouse, (Locatable)element, key);
		this.listeners = listeners;
		this.webDriver = webDriver;
	}

	@Override
	public void perform() {
		for (ActionsListener listener: listeners) {
			listener.beforeKeyUp(key, webDriver);
		}
		
		super.perform();
		
		for (ActionsListener listener: listeners) {
			listener.afterKeyUp(key, webDriver);
		}
	}
}