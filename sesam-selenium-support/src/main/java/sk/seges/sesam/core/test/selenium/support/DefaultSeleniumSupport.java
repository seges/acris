package sk.seges.sesam.core.test.selenium.support;

import java.util.Random;

import org.openqa.selenium.WebDriver;

import sk.seges.sesam.core.test.selenium.support.api.SeleniumSupport;

public class DefaultSeleniumSupport extends AbstractBrowserSupport implements SeleniumSupport {

//	protected DefaultSelenium selenium;
	protected WebDriver webDriver;

	private final Random random = new Random();

	private static final char[] symbols = new char[36];

	static {
		for (int idx = 0; idx < 10; ++idx)
			symbols[idx] = (char) ('0' + idx);
		for (int idx = 10; idx < 36; ++idx)
			symbols[idx] = (char) ('a' + idx - 10);
	}

	public DefaultSeleniumSupport(WebDriver webDriver) {
		this.webDriver = webDriver;
	}

	@Override
	public String getRandomString(int length) {
		if (length < 1) {
			throw new IllegalArgumentException("length < 1: " + length);
		}

		char[] buf = new char[length];
		for (int idx = 0; idx < buf.length; ++idx)
			buf[idx] = symbols[random.nextInt(symbols.length)];
		return new String(buf);
	}

//	@Override
//	public void waitForAlertPresent() {
//		waitForAction(new ActionHandler() {
//			public boolean doAction() {
//				Alert alert = webDriver.switchTo().alert();
//				return alert != null;
//			}
//		});
//	}
//
//	@Override
//	public void waitForAlert(final String pattern) {
//		waitForAction(new ActionHandler() {
//			public boolean doAction() {
//				Alert alert = webDriver.switchTo().alert();
//				return alert != null && alert.getText().contains(pattern);
//			}
//		});
//	}
	
//	@Override
//	public void waitForTextPresent(final String xpath) {
//		waitForAction(new ActionHandler() {
//			public boolean doAction() {
//				webDriver.findElement()
//				return selenium.isTextPresent(xpath);
//			}
//		});
//	}

//	@Override
//	public void waitForElementPresent(final String xpath) {
//		waitForAction(new ActionHandler() {
//			public boolean doAction() {
//				return selenium.isElementPresent(xpath);
//			}
//		});
//	}

//	@Override
//	public void waitAndClick(String xpath) {
//		webDriver.findElement(By.xpath(xpath)).click();
//	}
//
//	@Override
//	public void waitForTextsPresent(String... xpaths) {
//		for (String xpath: xpaths) {
//			waitForTextPresent(xpath);
//		}
//	}
//
//	@Override
//	public void waitForElementsPresent(String... xpaths) {
//		for (String xpath: xpaths) {
//			waitForElementPresent(xpath);
//		}
//	}

//	@Override
//	public void clickOnElement(String xpath) {
//		WebElement element = webDriver.findElement(By.xpath(xpath));
//		element.click();
//		selenium.mouseOver(xpath);
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			fail(e);
//		}
//		selenium.mouseDown(xpath);
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			fail(e);
//		}
//		selenium.mouseOver(xpath);
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			fail(e);
//		}
//		selenium.mouseUp(xpath);
//		try {
//			Thread.sleep(100);
//		} catch (InterruptedException e) {
//			fail(e);
//		}
//	}
//
//	@Override
//	public void waitAndType(String xpath, String text) {
//		waitForElementPresent(xpath);
//		selenium.type(xpath, text);
//	}
//
//	@Override
//	public void waitAndTypeKeys(String xpath, String text) {
//		waitForElementPresent(xpath);
//		selenium.typeKeys(xpath, text);
//	}

	@Override
	public String getRandomEmail() {
		return getRandomString(6) + "@" + getRandomString(6) + "." + getRandomString(2);
	}
}