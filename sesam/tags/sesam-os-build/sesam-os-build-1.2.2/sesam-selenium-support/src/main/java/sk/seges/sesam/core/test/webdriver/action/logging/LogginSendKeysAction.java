package sk.seges.sesam.core.test.webdriver.action.logging;

import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.SendKeysAction;
import org.openqa.selenium.internal.Locatable;

import sk.seges.sesam.core.test.webdriver.report.ActionsListener;

public class LogginSendKeysAction extends SendKeysAction {

	private final ActionsListener listener;
	private final WebDriver webDriver;
	private final CharSequence[] keysToSend;
	
	public LogginSendKeysAction(Keyboard keyboard, Mouse mouse, WebElement element, CharSequence[] keysToSend, ActionsListener listener, WebDriver webDriver) {
		super(keyboard, mouse, (Locatable)element, keysToSend);
		this.listener = listener;
		this.keysToSend = keysToSend;
		this.webDriver = webDriver;
	}

	@Override
	public void perform() {
		listener.beforeSendKeys(keysToSend, webDriver);
		super.perform();
		listener.afterSendKeys(keysToSend, webDriver);
	}

}