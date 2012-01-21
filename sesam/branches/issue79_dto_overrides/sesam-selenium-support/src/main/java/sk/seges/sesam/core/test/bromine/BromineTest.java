package sk.seges.sesam.core.test.bromine;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;

import org.openqa.selenium.WebDriver;

import sk.seges.sesam.core.test.bromine.exception.BromineException;
import sk.seges.sesam.core.test.bromine.request.BromineRequest;
import sk.seges.sesam.core.test.bromine.utils.QueryHelper;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSettings;
import sk.seges.sesam.core.test.selenium.factory.LocalWebDriverFactory;
import sk.seges.sesam.core.test.selenium.factory.RemoteWebDriverFactory;
import sk.seges.sesam.core.test.selenium.factory.WebDriverFactory;
import sk.seges.sesam.core.test.selenium.support.event.AssertionEventListener;
import sk.seges.sesam.core.test.selenium.support.event.AssertionEventListener.ComparationType;

public abstract class BromineTest implements Assertion {

	protected SeleniumSettings testEnvironment;

	protected WebDriver webDriver;

//	private Boolean bromineAccessible;
	
	private List<AssertionEventListener> listeners = new ArrayList<AssertionEventListener>();
	
	protected BromineTest() {
	}

	protected void registerAssertionListener(AssertionEventListener listener) {
		listeners.add(listener);
	}
	
	public void setTestEnvironment(SeleniumSettings testEnvironment) {
		this.testEnvironment = testEnvironment;
		webDriver = createWebDriver(getWebDriverFactory(testEnvironment), testEnvironment);
	}

	protected WebDriver createWebDriver(WebDriverFactory webDriverFactory, SeleniumSettings seleniumSettings) {
		return webDriverFactory.createSelenium(seleniumSettings);
	}
	
	protected WebDriverFactory getWebDriverFactory(SeleniumSettings testEnvironment) {
		return testEnvironment.getSeleniumRemote() ? new RemoteWebDriverFactory() : new LocalWebDriverFactory();
	}

	public void tearDown() {
		webDriver.quit();
	}

	public void runTests() {};

	protected abstract String getTestName();

	protected boolean supportsBromine() {
		return (testEnvironment.getBromine() != null &&
				testEnvironment.getBromine().equals(Boolean.TRUE));
		
//		if (bromineAccessible != null) {
//			return bromineAccessible;
//		}
//
//		String result;
//		try {
//			result = new BromineRequest(testEnvironment.getBromineEnvironment()).send(null, null);
//		} catch (Exception e) {
//			bromineAccessible = false;
//			System.out.println("Bromine is not accessible. Using local mode only!");
//			return bromineAccessible;
//		}
//
//		bromineAccessible = (result.indexOf("OK") > -1);
//		if (!bromineAccessible) {
//			System.out.println("Bromine is not accessible. Using local mode only!");
//		}
//		return bromineAccessible;
	}
	
	public synchronized String executeBromineQuery(BromineCommand command) {
		String result = "";

		Hashtable<String, Object> functionParams = command.to();
		functionParams.put("test_id", getTestName());

		Hashtable<String, Object> data = new Hashtable<String, Object>();
		Enumeration<String> paramsEnumetation = functionParams.keys();

		while (paramsEnumetation.hasMoreElements()) {
			String key = (String) paramsEnumetation.nextElement();
			Object value = functionParams.get(key);

			Hashtable<String, Object> nested = new Hashtable<String, Object>();

			if (value instanceof Boolean) {
				nested.put("value", (((Boolean) value) == false) ? "0" : "1");
				nested.put("type", "Boolean");
			} else if (value instanceof String) {
				nested.put("value", (String) value);
				nested.put("type", "String");
			}

			data.put(key, nested);
		}

		String query = null;

		try {
			query = QueryHelper.toQueryString(data, "");
		} catch (UnsupportedEncodingException e) {
			this.tearDown();
			throw new BromineException("Unable to create bormine request.", e);
		}

		try {
			result = new BromineRequest(testEnvironment).send(command, query);

			if (result.indexOf("OK") != 0) {
				this.tearDown();
				throw new BromineException("Bromine controller returned error:" + result + " in function " + command.getName());
			}
		} catch (Exception ex) {
			this.tearDown();
			throw new BromineException("Bromine controller returned error:" + result + " in function " + command.getName());
		}

		return result;
	}

	private synchronized void customCommand(String action, String status, String statement1, String statement2) {
		customCommand(action, status, statement1, statement2, "");
	}

	private synchronized void customCommand(String action, String status, String statement1, String statement2, String comment) {
		executeBromineQuery(BromineCommand.CUSTOM.get().action(action).statement1(statement1).statement2(statement2) .status(status).comment(comment));
	}

	private synchronized void assertNotFailed(String result) {

		if (result.split(",")[1].indexOf("failed") == 0) {
			customCommand("Assert failed", "failed", "Assertion failed. Test stopped!", "");
			throw new RuntimeException("Statement assertion failed with result " + result);
//			tearDown();
//			System.exit(-1);
		}
	}

	public synchronized void assertTrue(Boolean statement1) {
		assertTrue(statement1, "");
	}

	public synchronized void assertTrue(Boolean statement1, String comment) {
		if (supportsBromine()) {
			assertNotFailed(executeBromineQuery(BromineCommand.ASSERT_TRUE.get().statement1(statement1).comment(comment)));
		} else {
			if (statement1 == null || statement1.equals(Boolean.FALSE)) {
				for (AssertionEventListener assertionListener: listeners) {
					assertionListener.onAssertion(false, statement1, ComparationType.POSITIVE, comment);
				}
				throw new RuntimeException("Statement assertion failed. [Expected true] " + comment);
			}
		}
		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onAssertion(true, statement1, ComparationType.POSITIVE, comment);
		}
	}

	public synchronized void assertFalse(Boolean statement1) {
		assertFalse(statement1, "");
	}

	public synchronized void assertFalse(Boolean statement1, String comment) {
		if (supportsBromine()) {
			assertNotFailed(executeBromineQuery(BromineCommand.ASSERT_FALSE.get().statement1(statement1).comment(comment)));
		} else {
			if (statement1 == null || statement1.equals(Boolean.TRUE)) {
				for (AssertionEventListener assertionListener: listeners) {
					assertionListener.onAssertion(false, statement1, ComparationType.NEGATIVE, comment);
				}
				throw new RuntimeException("Statement assertion failed. [Expected false] " + comment);
			}
		}
		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onAssertion(true, statement1, ComparationType.NEGATIVE, comment);
		}
	}

	public synchronized void assertEquals(String statement1, String statement2) {
		assertEquals(statement1, statement2, "");
	}

	public synchronized void assertEquals(String statement1, String statement2, String comment) {
		if (supportsBromine()) {
			assertNotFailed(executeBromineQuery(BromineCommand.ASSERT_EQUALS.get().statement1(statement1).statement2(statement2).comment(comment)));
		} else {
			if (statement1 == null) {
				if (statement2 != null) {
					for (AssertionEventListener assertionListener: listeners) {
						assertionListener.onAssertion(false, statement1, statement2, ComparationType.POSITIVE, comment);
					}
					throw new RuntimeException("Statements assertion failed. [" + statement1 + " should equals " + statement2 + "] " + comment);
				}
			} else {
				if (statement2 == null || !statement1.equals(statement2)) {
					for (AssertionEventListener assertionListener: listeners) {
						assertionListener.onAssertion(false, statement1, statement2, ComparationType.POSITIVE, comment);
					}
					throw new RuntimeException("Statements assertion failed. [" + statement1 + " should equals " + statement2 + "] " + comment);
				}
			}
		}
		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onAssertion(true, statement1, statement2, ComparationType.POSITIVE, comment);
		}
	}

	public synchronized void assertNotEquals(String statement1, String statement2) {
		assertNotEquals(statement1, statement2, "");
	}

	public synchronized void assertNotEquals(String statement1, String statement2, String comment) {
		if (supportsBromine()) {
			assertNotFailed(executeBromineQuery(BromineCommand.ASSERT_NOT_EQUALS.get().statement1(statement1)
					.statement2(statement2).comment(comment)));
		} else {
			if (statement1 == null && statement2 == null) {
				for (AssertionEventListener assertionListener: listeners) {
					assertionListener.onAssertion(false, statement1, statement2, ComparationType.NEGATIVE, comment);
				}
				throw new RuntimeException("Statements assertion failed. [" + statement1 + " should not equals "
						+ statement2 + "] " + comment);
			} else {
				for (AssertionEventListener assertionListener: listeners) {
					assertionListener.onAssertion(false, statement1, statement2, ComparationType.NEGATIVE, comment);
				}
				if ((statement1 != null && statement1.equals(statement2)) || (statement2 != null && statement2.equals(statement1))) {
					throw new RuntimeException("Statements assertion failed. [" + statement1 + " should not equals "
							+ statement2 + "] " + comment);
				}
			}
		}
		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onAssertion(true, statement1, statement2, ComparationType.NEGATIVE, comment);
		}
	}

	public synchronized void verifyTrue(Boolean statement1) {
		verifyTrue(statement1, "");
	}

	public synchronized void verifyTrue(Boolean statement1, String comment) {
		if (supportsBromine()) {
			executeBromineQuery(BromineCommand.VERIFY_TRUE.get().statement1(statement1).comment(comment));
		} else {
			if (statement1 == null || statement1.equals(Boolean.FALSE)) {
				for (AssertionEventListener assertionListener: listeners) {
					assertionListener.onVerification(false, statement1, ComparationType.POSITIVE, comment);
				}
				throw new RuntimeException("Statement verification failed. [Expected true] " + comment);
			}
		}
		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onVerification(true, statement1, ComparationType.POSITIVE, comment);
		}
	}

	public synchronized void verifyFalse(Boolean statement1) {
		verifyFalse(statement1, "");
	}

	public synchronized void verifyFalse(Boolean statement1, String comment) {
		if (supportsBromine()) {
			executeBromineQuery(BromineCommand.VERIFY_FALSE.get().statement1(statement1).comment(comment));
		} else {
			if (statement1 == null || statement1.equals(Boolean.TRUE)) {
				for (AssertionEventListener assertionListener: listeners) {
					assertionListener.onVerification(false, statement1, ComparationType.NEGATIVE, comment);
				}
				throw new RuntimeException("Statement verification failed. [Expected false] " + comment);
			}
		}
		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onVerification(true, statement1, ComparationType.NEGATIVE, comment);
		}
	}

	public synchronized void verifyEquals(String statement1, String statement2) {
		verifyEquals(statement1, statement2, "");
	}

	public synchronized void verifyEquals(String statement1, String statement2, String comment) {
		if (supportsBromine()) {
			executeBromineQuery(BromineCommand.VERIFY_EQUALS.get().statement1(statement1).statement2(statement2).comment(comment));
		} else {
			if (statement1 == null) {
				if (statement2 != null) {
					for (AssertionEventListener assertionListener: listeners) {
						assertionListener.onVerification(false, statement1, statement2, ComparationType.POSITIVE, comment);
					}
					throw new RuntimeException("Statements verification failed. [" + statement1 + " should equals " + statement2 + "] " + comment);
				}
			} else {
				if (statement2 == null || !statement1.equals(statement2)) {
					for (AssertionEventListener assertionListener: listeners) {
						assertionListener.onVerification(false, statement1, statement2, ComparationType.POSITIVE, comment);
					}
					throw new RuntimeException("Statements verification failed. [" + statement1 + " should equals " + statement2 + "] " + comment);
				}
			}
		}
		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onVerification(true, statement1, statement2, ComparationType.POSITIVE, comment);
		}
	}

	public synchronized void verifyNotEquals(String statement1, String statement2) {
		verifyNotEquals(statement1, statement2, "");
	}

	public synchronized void verifyNotEquals(String statement1, String statement2, String comment) {
		if (supportsBromine()) {
			executeBromineQuery(BromineCommand.VERIFY_NOT_EQUALS.get().statement1(statement1).statement2(statement2).comment(comment));
		} else {
			if (statement1 == null) {
				if (statement2 == null) {
					for (AssertionEventListener assertionListener: listeners) {
						assertionListener.onVerification(false, statement1, statement2, ComparationType.NEGATIVE, comment);
					}
					throw new RuntimeException("Statements verification failed. [" + statement1 + " should not equals " + statement2 + "] " + comment);
				}
			} else {
				if (statement2 != null || statement1.equals(statement2)) {
					for (AssertionEventListener assertionListener: listeners) {
						assertionListener.onVerification(false, statement1, statement2, ComparationType.NEGATIVE, comment);
					}
					throw new RuntimeException("Statements verification failed. [" + statement1 + " should not equals " + statement2 + "] " + comment);
				}
			}
		}
		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onVerification(true, statement1, statement2, ComparationType.NEGATIVE, comment);
		}
	}

	public synchronized void waiting() {
		if (supportsBromine()) {
			this.ignore();
		}
	}

	public synchronized void ignore() {
		if (supportsBromine()) {
			executeBromineQuery(BromineCommand.WAITING.get().action("waiting").comment(""));
		}
	}
}