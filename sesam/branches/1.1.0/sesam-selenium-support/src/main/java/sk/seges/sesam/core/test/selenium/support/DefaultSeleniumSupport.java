package sk.seges.sesam.core.test.selenium.support;

import sk.seges.sesam.core.test.selenium.support.api.SeleniumSupport;

import com.thoughtworks.selenium.DefaultSelenium;

public class DefaultSeleniumSupport extends AbstractBrowserSupport implements SeleniumSupport {

	private DefaultSelenium selenium;

	public DefaultSeleniumSupport(DefaultSelenium selenium) {
		this.selenium = selenium;
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
}