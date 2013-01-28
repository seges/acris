package sk.seges.sesam.core.test.webdriver.action.logging;

import java.util.List;

import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.MoveToOffsetAction;
import org.openqa.selenium.internal.Locatable;

import sk.seges.sesam.core.test.webdriver.report.ActionsListener;

public class LoggingMoveToOffsetAction extends MoveToOffsetAction {

	private final List<? extends ActionsListener> listeners;
	private final WebDriver webDriver;
	private final WebElement webElement;
	
	private final int xOffset;
	private final int yOffset;

	public LoggingMoveToOffsetAction(Mouse mouse, WebElement onElement, int x, int y, List<? extends ActionsListener> listeners, WebDriver webDriver) {
		super(mouse, (Locatable)onElement, x, y);
		this.listeners = listeners;
		this.xOffset = x;
		this.yOffset = y;
		this.webDriver = webDriver;
		this.webElement = onElement;
	}
	
	@Override
	public void perform() {
		for (ActionsListener listener: listeners) {
			listener.beforeMoveToOffset(webElement, xOffset, yOffset,  webDriver);
		}
		
		super.perform();
		
		for (ActionsListener listener: listeners) {
			listener.afterMoveToOffset(webElement, xOffset, yOffset,  webDriver);
		}
	}
}
