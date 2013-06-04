package sk.seges.sesam.core.test.webdriver;

import org.openqa.selenium.HasInputDevices;
import org.openqa.selenium.Keyboard;
import org.openqa.selenium.Keys;
import org.openqa.selenium.Mouse;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.ButtonReleaseAction;
import org.openqa.selenium.interactions.ClickAction;
import org.openqa.selenium.interactions.ClickAndHoldAction;
import org.openqa.selenium.interactions.CompositeAction;
import org.openqa.selenium.interactions.ContextClickAction;
import org.openqa.selenium.interactions.KeyDownAction;
import org.openqa.selenium.interactions.KeyUpAction;
import org.openqa.selenium.interactions.MoveMouseAction;
import org.openqa.selenium.interactions.MoveToOffsetAction;
import org.openqa.selenium.interactions.SendKeysAction;
import org.openqa.selenium.internal.Locatable;

import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;
import sk.seges.sesam.core.test.webdriver.action.DoubleClickAction;

public class WebDriverActions extends Actions {

	protected TestEnvironment testEnvironment;

	protected Mouse mouse;
	protected Keyboard keyboard;
	private CompositeAction action;
	protected WebDriver webDriver;
	
	public WebDriverActions(WebDriver webDriver, TestEnvironment testEnvironment) {
		this(webDriver, ((HasInputDevices) webDriver).getKeyboard(), ((HasInputDevices) webDriver).getMouse(), testEnvironment);
	}

	public WebDriverActions(WebDriver webDriver, Keyboard keyboard, Mouse mouse, TestEnvironment testEnvironment) {
		super(keyboard, mouse);
		this.mouse = mouse;
		this.keyboard = keyboard;
		action = new CompositeAction();
		this.testEnvironment = testEnvironment;
		this.webDriver = webDriver;
	}

	public Actions keyDown(Keys theKey) {
		return this.keyDown(null, theKey);
	}

	public Actions keyDown(WebElement element, Keys theKey) {
		action.addAction(new KeyDownAction(keyboard, mouse, (Locatable) element, theKey));
		return this;
	}

	public Actions keyUp(Keys theKey) {
		return this.keyUp(null, theKey);
	}

	public Actions keyUp(WebElement element, Keys theKey) {
		action.addAction(new KeyUpAction(keyboard, mouse, (Locatable) element, theKey));
		return this;
	}

	public Actions sendKeys(CharSequence... keysToSend) {
		return this.sendKeys(null, keysToSend);
	}

	public Actions sendKeys(WebElement element, CharSequence... keysToSend) {
		action.addAction(new SendKeysAction(keyboard, mouse, (Locatable) element, keysToSend));
		return this;
	}

	public Actions clickAndHold(WebElement onElement) {
		action.addAction(new ClickAndHoldAction(mouse, (Locatable) onElement));
		return this;
	}

	public Actions release(WebElement onElement) {
		action.addAction(new ButtonReleaseAction(mouse, (Locatable) onElement));
		return this;
	}

	public Actions click(WebElement onElement) {
		action.addAction(new ClickAction(mouse, (Locatable) onElement));
		return this;
	}

	public Actions click() {
		return this.click(null);
	}

	public Actions moveToElement(WebElement toElement) {
		action.addAction(new MoveMouseAction(mouse, (Locatable) toElement));
		return this;
	}

	public Actions moveToElement(WebElement toElement, int xOffset, int yOffset) {
		action.addAction(new MoveToOffsetAction(mouse, (Locatable) toElement, xOffset, yOffset));
		return this;
	}

	public Actions moveByOffset(int xOffset, int yOffset) {
		action.addAction(new MoveToOffsetAction(mouse, null, xOffset, yOffset));
		return this;
	}

	public Actions contextClick(WebElement onElement) {
		action.addAction(new ContextClickAction(mouse, (Locatable) onElement));
		return this;
	}

	public Actions dragAndDrop(WebElement source, WebElement target) {
		action.addAction(new ClickAndHoldAction(mouse, (Locatable) source));
		action.addAction(new MoveMouseAction(mouse, (Locatable) target));
		action.addAction(new ButtonReleaseAction(mouse, (Locatable) target));
		return this;
	}

	public Action build() {
		CompositeAction toReturn = action;
		action = new CompositeAction();
		return toReturn;
	}

	public void perform() {
		build().perform();
	}

	@Override
	public Actions doubleClick(WebElement onElement) {
		try {
			action.addAction(new DoubleClickAction(webDriver, testEnvironment, onElement));
		} catch (Exception e) {
			action.addAction(new org.openqa.selenium.interactions.DoubleClickAction(mouse, (Locatable) onElement));
		}
		
		return this;
	}
}