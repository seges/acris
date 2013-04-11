package sk.seges.sesam.core.test.webdriver.support;

import sk.seges.sesam.core.test.webdriver.exception.WebdriverException;

public abstract class AbstractBrowserSupport {

	public interface ActionHandler {
		boolean doAction();
	}

	public void fail(String message) {
		throw new WebdriverException(message);
	}

	public void fail(Exception e) {
		throw new WebdriverException(e);
	}
}