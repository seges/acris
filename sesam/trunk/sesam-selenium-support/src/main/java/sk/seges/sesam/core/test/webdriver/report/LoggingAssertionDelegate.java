package sk.seges.sesam.core.test.webdriver.report;

import java.util.ArrayList;
import java.util.List;

import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperationState;
import sk.seges.sesam.core.test.webdriver.JunitAssertionDelegate;
import sk.seges.sesam.core.test.webdriver.JunitAssertionWrapper;
import sk.seges.sesam.core.test.webdriver.support.event.AssertionEventListener;

public class LoggingAssertionDelegate extends JunitAssertionDelegate {

	private List<AssertionEventListener> listeners = new ArrayList<AssertionEventListener>();

	public LoggingAssertionDelegate(JunitAssertionWrapper assertion) {
		super(assertion);
	}

	public void registerAssertionListener(AssertionEventListener listener) {
		listeners.add(listener);
	}

	protected void beforeFail(String message) {
		for (AssertionEventListener eventListener: listeners) {
			eventListener.onFail(SeleniumOperationState.BEFORE, message);
		}
	}
	
	protected void afterFail(String message) {
		for (AssertionEventListener eventListener: listeners) {
			eventListener.onFail(SeleniumOperationState.AFTER, message);
		}
	}
	
	protected AssertionResult beforeAssert(AssertionResult result) {
		for (AssertionEventListener eventListener: listeners) {
			eventListener.onAssertion(SeleniumOperationState.BEFORE, result);
		}
		
		return result;
	};
	
	protected void afterAssert(AssertionResult assertionResult) {
		for (AssertionEventListener eventListener: listeners) {
			eventListener.onAssertion(SeleniumOperationState.AFTER, assertionResult);
		}
	};
}