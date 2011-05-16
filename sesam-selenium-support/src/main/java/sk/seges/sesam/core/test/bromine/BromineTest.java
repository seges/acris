package sk.seges.sesam.core.test.bromine;

import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.Hashtable;

import org.openqa.selenium.WebDriver;

import sk.seges.sesam.core.test.bromine.exception.BromineException;
import sk.seges.sesam.core.test.bromine.request.BromineRequest;
import sk.seges.sesam.core.test.bromine.utils.QueryHelper;
import sk.seges.sesam.core.test.selenium.configuration.api.TestEnvironment;
import sk.seges.sesam.core.test.selenium.factory.LocalWebDriverFactory;
import sk.seges.sesam.core.test.selenium.factory.RemoteWebDriverFactory;
import sk.seges.sesam.core.test.selenium.factory.WebDriverFactory;

public abstract class BromineTest {

	protected TestEnvironment testEnvironment;

	protected WebDriver webDriver;

//	private Boolean bromineAccessible;
	
	protected BromineTest() {
	}

	public void setTestEnvironment(TestEnvironment testEnvironment) {
		this.testEnvironment = testEnvironment;
		webDriver = getWebDriverFactory(testEnvironment).createSelenium(testEnvironment);
	}

	protected WebDriverFactory getWebDriverFactory(TestEnvironment testEnvironment) {
		return testEnvironment.isRemote() ? new RemoteWebDriverFactory() : new LocalWebDriverFactory();
	}

	public void tearDown() {
		webDriver.close();
		webDriver.quit();
	}

	public void runTests() {};

	protected abstract String getTestName();

	protected boolean supportsBromine() {
		return (testEnvironment.getBromineEnvironment().isBromineEnabled() != null &&
				testEnvironment.getBromineEnvironment().isBromineEnabled().equals(Boolean.TRUE));
		
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
			result = new BromineRequest(testEnvironment.getBromineEnvironment()).send(command, query);

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
			tearDown();
			System.exit(-1);
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
				throw new RuntimeException("Statement assertion failed. [Expected true] " + comment);
			}
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
				throw new RuntimeException("Statement assertion failed. [Expected false] " + comment);
			}
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
					throw new RuntimeException("Statements assertion failed. [" + statement1 + " should equals " + statement2 + "] " + comment);
				}
			} else {
				if (statement2 == null || !statement1.equals(statement2)) {
					throw new RuntimeException("Statements assertion failed. [" + statement1 + " should equals " + statement2 + "] " + comment);
				}
			}
		}
	}

	public synchronized void assertNotEquals(String statement1, String statement2) {
		assertNotEquals(statement1, statement2, "");
	}

	public synchronized void assertNotEquals(String statement1, String statement2, String comment) {
		if (supportsBromine()) {
			assertNotFailed(executeBromineQuery(BromineCommand.ASSERT_NOT_EQUALS.get().statement1(statement1).statement2(statement2).comment(comment)));
		} else {
			if (statement1 == null) {
				if (statement2 == null) {
					throw new RuntimeException("Statements assertion failed. [" + statement1 + " should not equals " + statement2 + "] " + comment);
				}
			} else {
				if (statement2 != null || statement1.equals(statement2)) {
					throw new RuntimeException("Statements assertion failed. [" + statement1 + " should not equals " + statement2 + "] " + comment);
				}
			}
		}
	}

	//TODO This should not throws exception
	public synchronized void verifyTrue(Boolean statement1) {
		verifyTrue(statement1, "");
	}

	//TODO This should not throws exception
	public synchronized void verifyTrue(Boolean statement1, String comment) {
		if (supportsBromine()) {
			executeBromineQuery(BromineCommand.VERIFY_TRUE.get().statement1(statement1).comment(comment));
		} else {
			if (statement1 == null || statement1.equals(Boolean.FALSE)) {
				throw new RuntimeException("Statement verification failed. [Expected true] " + comment);
			}
		}
	}

	//TODO This should not throws exception
	public synchronized void verifyFalse(Boolean statement1) {
		verifyFalse(statement1, "");
	}

	//TODO This should not throws exception
	public synchronized void verifyFalse(Boolean statement1, String comment) {
		if (supportsBromine()) {
			executeBromineQuery(BromineCommand.VERIFY_FALSE.get().statement1(statement1).comment(comment));
		} else {
			if (statement1 == null || statement1.equals(Boolean.TRUE)) {
				throw new RuntimeException("Statement verification failed. [Expected false] " + comment);
			}
		}
	}

	//TODO This should not throws exception
	public synchronized void verifyEquals(String statement1, String statement2) {
		verifyEquals(statement1, statement2, "");
	}

	//TODO This should not throws exception
	public synchronized void verifyEquals(String statement1, String statement2, String comment) {
		if (supportsBromine()) {
			executeBromineQuery(BromineCommand.VERIFY_EQUALS.get().statement1(statement1).statement2(statement2).comment(comment));
		} else {
			if (statement1 == null) {
				if (statement2 != null) {
					throw new RuntimeException("Statements verification failed. [" + statement1 + " should equals " + statement2 + "] " + comment);
				}
			} else {
				if (statement2 == null || !statement1.equals(statement2)) {
					throw new RuntimeException("Statements verification failed. [" + statement1 + " should equals " + statement2 + "] " + comment);
				}
			}
		}
	}

	//TODO This should not throws exception
	public synchronized void verifyNotEquals(String statement1, String statement2) {
		verifyNotEquals(statement1, statement2, "");
	}

	//TODO This should not throws exception
	public synchronized void verifyNotEquals(String statement1, String statement2, String comment) {
		if (supportsBromine()) {
			executeBromineQuery(BromineCommand.VERIFY_NOT_EQUALS.get().statement1(statement1).statement2(statement2).comment(comment));
		} else {
			if (statement1 == null) {
				if (statement2 == null) {
					throw new RuntimeException("Statements verification failed. [" + statement1 + " should not equals " + statement2 + "] " + comment);
				}
			} else {
				if (statement2 != null || statement1.equals(statement2)) {
					throw new RuntimeException("Statements verification failed. [" + statement1 + " should not equals " + statement2 + "] " + comment);
				}
			}
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