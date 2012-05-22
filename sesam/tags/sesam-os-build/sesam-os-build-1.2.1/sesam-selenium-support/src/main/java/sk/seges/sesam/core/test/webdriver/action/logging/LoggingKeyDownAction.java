package sk.seges.sesam.core.test.webdriver.action.logging;

import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.KeyDownAction;
import org.openqa.selenium.internal.Locatable;

import sk.seges.sesam.core.test.webdriver.report.ActionsListener;

public class LoggingKeyDownAction extends KeyDownAction {

	private final ActionsListener listener;
	private final WebDriver webDriver;
	
	public LoggingKeyDownAction(Keyboard keyboard, Mouse mouse, WebElement onElement, Keys key, ActionsListener listener, WebDriver webDriver) {
		super(keyboard, mouse, (Locatable)onElement, key);
		this.listener = listener;
		this.webDriver = webDriver;
	}

	@Override
	public void perform() {
		listener.beforeKeyDown(key, webDriver);
		super.perform();
		listener.afterKeyDown(key, webDriver);
	}
}
