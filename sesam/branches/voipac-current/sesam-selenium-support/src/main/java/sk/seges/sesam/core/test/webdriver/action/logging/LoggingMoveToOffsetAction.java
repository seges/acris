package sk.seges.sesam.core.test.webdriver.action.logging;

import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.MoveToOffsetAction;
import org.openqa.selenium.internal.Locatable;

import sk.seges.sesam.core.test.webdriver.report.ActionsListener;

public class LoggingMoveToOffsetAction extends MoveToOffsetAction {

	private final ActionsListener listener;
	private final WebDriver webDriver;
	private final WebElement webElement;
	
	private final int xOffset;
	private final int yOffset;

	public LoggingMoveToOffsetAction(Mouse mouse, WebElement onElement, int x, int y, ActionsListener listener, WebDriver webDriver) {
		super(mouse, (Locatable)onElement, x, y);
		this.listener = listener;
		this.xOffset = x;
		this.yOffset = y;
		this.webDriver = webDriver;
		this.webElement = onElement;
	}
	
	@Override
	public void perform() {
		listener.beforeMoveToOffset(webElement, xOffset, yOffset,  webDriver);
		super.perform();
		listener.afterMoveToOffset(webElement, xOffset, yOffset,  webDriver);
	}
}
