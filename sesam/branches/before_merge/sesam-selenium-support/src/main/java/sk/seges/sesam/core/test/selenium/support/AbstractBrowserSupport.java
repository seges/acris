package sk.seges.sesam.core.test.selenium.support;

import sk.seges.sesam.core.test.selenium.exception.SeleniumException;

public abstract class AbstractBrowserSupport {

	public interface ActionHandler {
		boolean doAction();
	}

	public void fail(String message) {
		throw new SeleniumException(message);
	}

	public void fail(Exception e) {
		throw new SeleniumException(e);
	}
}