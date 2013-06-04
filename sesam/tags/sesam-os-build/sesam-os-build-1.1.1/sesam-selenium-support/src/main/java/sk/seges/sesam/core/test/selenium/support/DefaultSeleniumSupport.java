package sk.seges.sesam.core.test.selenium.support;

import java.util.Random;

import sk.seges.sesam.core.test.selenium.support.api.SeleniumSupport;

import com.thoughtworks.selenium.DefaultSelenium;

public class DefaultSeleniumSupport extends AbstractBrowserSupport implements SeleniumSupport {

	protected DefaultSelenium selenium;

	private final Random random = new Random();

	private static final char[] symbols = new char[36];

	static {
		for (int idx = 0; idx < 10; ++idx)
			symbols[idx] = (char) ('0' + idx);
		for (int idx = 10; idx < 36; ++idx)
			symbols[idx] = (char) ('a' + idx - 10);
	}

	public DefaultSeleniumSupport(DefaultSelenium selenium) {
		this.selenium = selenium;
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

	@Override
	public void waitForAlertPresent() {
		waitForAction(new ActionHandler() {
			public boolean doAction() {
				return selenium.isAlertPresent();
			}
		});
	}

	@Override
	public void waitForAlert(final String pattern) {
		waitForAction(new ActionHandler() {
			public boolean doAction() {
				return pattern.equals(selenium.getAlert());
			}
		});
	}
	
	@Override
	public void waitForTextPresent(final String xpath) {
		waitForAction(new ActionHandler() {
			public boolean doAction() {
				return selenium.isTextPresent(xpath);
			}
		});
	}

	@Override
	public void waitForElementPresent(final String xpath) {
		waitForAction(new ActionHandler() {
			public boolean doAction() {
				return selenium.isElementPresent(xpath);
			}
		});
	}

	@Override
	public void waitAndClick(String xpath) {
		waitForElementPresent(xpath);
		selenium.click(xpath);
	}

	@Override
	public void waitForTextsPresent(String... xpaths) {
		for (String xpath: xpaths) {
			waitForTextPresent(xpath);
		}
	}

	@Override
	public void waitForElementsPresent(String... xpaths) {
		for (String xpath: xpaths) {
			waitForElementPresent(xpath);
		}
	}

	@Override
	public void clickOnElement(String xpath) {
		selenium.mouseOver(xpath);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			fail(e);
		}
		selenium.mouseDown(xpath);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			fail(e);
		}
		selenium.mouseOver(xpath);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			fail(e);
		}
		selenium.mouseUp(xpath);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			fail(e);
		}
	}
}