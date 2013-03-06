package sk.seges.sesam.core.test.webdriver.support.event;

import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperationState;
import sk.seges.sesam.core.test.webdriver.JunitAssertionDelegate.AssertionResult;

public interface AssertionEventListener {

	void onAssertion(SeleniumOperationState state, AssertionResult assertionResult);
	
	void onFail(SeleniumOperationState state, String mesage);
}