package sk.seges.sesam.core.test.webdriver.action.logging;

import java.util.List;

import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.KeyDownAction;
import org.openqa.selenium.internal.Locatable;

import sk.seges.sesam.core.test.webdriver.report.ActionsListener;

public class LoggingKeyDownAction extends KeyDownAction {

	private final List<? extends ActionsListener> listeners;
	private final WebDriver webDriver;
	
	public LoggingKeyDownAction(Keyboard keyboard, Mouse mouse, WebElement onElement, Keys key, List<? extends ActionsListener> listeners, WebDriver webDriver) {
		super(keyboard, mouse, (Locatable)onElement, key);
		this.listeners = listeners;
		this.webDriver = webDriver;
	}

	@Override
	public void perform() {
		for (ActionsListener listener: listeners) {
			listener.beforeKeyDown(key, webDriver);
		}

		super.perform();
		
		for (ActionsListener listener: listeners) {
			listener.afterKeyDown(key, webDriver);
		}
	}
}
