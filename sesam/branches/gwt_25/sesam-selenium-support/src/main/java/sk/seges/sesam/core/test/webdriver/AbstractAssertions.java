package sk.seges.sesam.core.test.webdriver;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.WebDriver;

import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumSettings;
import sk.seges.sesam.core.test.webdriver.api.Assertion;
import sk.seges.sesam.core.test.webdriver.api.SesamWebDriver;
import sk.seges.sesam.core.test.webdriver.factory.LocalWebDriverFactory;
import sk.seges.sesam.core.test.webdriver.factory.RemoteWebDriverFactory;
import sk.seges.sesam.core.test.webdriver.factory.WebDriverFactory;
import sk.seges.sesam.core.test.webdriver.model.SesamWebDriverDelegate;
import sk.seges.sesam.core.test.webdriver.support.event.AssertionEventListener;
import sk.seges.sesam.core.test.webdriver.support.event.AssertionEventListener.ComparationType;

public abstract class AbstractAssertions implements Assertion {

	protected SeleniumSettings testEnvironment;

	protected SesamWebDriver webDriver;

	private List<AssertionEventListener> listeners = new ArrayList<AssertionEventListener>();
	
	protected AbstractAssertions() {
	}

	protected void registerAssertionListener(AssertionEventListener listener) {
		listeners.add(listener);
	}
	
	public void setTestEnvironment(SeleniumSettings testEnvironment) {
		this.testEnvironment = testEnvironment;
		webDriver = new SesamWebDriverDelegate(createWebDriver(getWebDriverFactory(testEnvironment), testEnvironment));
	}

	protected WebDriver createWebDriver(WebDriverFactory webDriverFactory, SeleniumSettings seleniumSettings) {
		return webDriverFactory.createSelenium(seleniumSettings);
	}
	
	protected WebDriverFactory getWebDriverFactory(SeleniumSettings testEnvironment) {
		return testEnvironment.getRemoteServerURL() == null ? 
				new LocalWebDriverFactory() : new RemoteWebDriverFactory();
	}

	public void tearDown() {
		webDriver.quit();
	}

	public void runTests() {};

	protected abstract String getTestName();

	@Override
	public synchronized void assertTrue(Boolean statement1) {
		assertTrue(statement1, "");
	}
	
	@Override
	public synchronized void assertTrue(Boolean statement1, String comment) {
		if (statement1 == null || statement1.equals(Boolean.FALSE)) {
			for (AssertionEventListener assertionListener: listeners) {
				assertionListener.onAssertion(false, statement1, ComparationType.POSITIVE, comment);
			}
			throw new RuntimeException("Statement assertion failed. [Expected true] " + comment);
		}
		
		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onAssertion(true, statement1, ComparationType.POSITIVE, comment);
		}
	}

	@Override
	public synchronized void assertFalse(Boolean statement1) {
		assertFalse(statement1, "");
	}

	@Override
	public synchronized void assertFalse(Boolean statement1, String comment) {
		if (statement1 == null || statement1.equals(Boolean.TRUE)) {
			for (AssertionEventListener assertionListener: listeners) {
				assertionListener.onAssertion(false, statement1, ComparationType.NEGATIVE, comment);
			}
			throw new RuntimeException("Statement assertion failed. [Expected false] " + comment);
		}

		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onAssertion(true, statement1, ComparationType.NEGATIVE, comment);
		}
	}

	@Override
	public synchronized void assertEquals(Object statement1, Object statement2) {
		assertEquals(statement1, statement2, "");
	}

	private int getDifferenceIndex(String s1, String s2) {
		if (s1 == null || s2 == null) {
			return 0;
		}
		
		int count = Math.min(s1.length(), s2.length());
		
		int i = 0;
		
		for (i = 0; i < count; i++) {
			if (s1.charAt(i) != s2.charAt(i)) {
				return i;
			}
		}
		
		return i;
	}
	
	private String formatNotEqualString(String s, int index) {
		if (s == null) {
			return s;	
		}

		if (s.length() <= index) {
			return s + "[]";
		}
		
		
		if (s.length() + 1 == index) {
			return s.substring(0, index) + "[-->" + s.substring(index, 1) + "<--]";
		}
		return s.substring(0, index) + "[-->" + s.substring(index, 1) + "<--]" + s.substring(index + 1);
	}
	
	@Override
	public synchronized void assertEquals(Object statement1, Object statement2, String comment) {
		
		if (statement1 != null && statement2 != null && statement1 instanceof String && statement2 instanceof String) {
			assertEquals((String)statement1, (String)statement2, comment);
			return;
		}
		
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

		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onAssertion(true, statement1, statement2, ComparationType.POSITIVE, comment);
		}
	}

	private synchronized void assertEquals(String statement1, String statement2, String comment) {
		if (statement1 == null) {
			if (statement2 != null) {
				for (AssertionEventListener assertionListener: listeners) {
					assertionListener.onAssertion(false, statement1, statement2, ComparationType.POSITIVE, comment);
				}
				int index = getDifferenceIndex(statement1, statement2);
				
				throw new RuntimeException("Statements assertion failed. [" + formatNotEqualString(statement1, index) + " should equals " + 
						formatNotEqualString(statement2, index) + "] " + comment);
			}
		} else {
			if (statement2 == null || !statement1.equals(statement2)) {
				for (AssertionEventListener assertionListener: listeners) {
					assertionListener.onAssertion(false, statement1, statement2, ComparationType.POSITIVE, comment);
				}
				int index = getDifferenceIndex(statement1, statement2);
				throw new RuntimeException("Statements assertion failed. [" + formatNotEqualString(statement1, index) + " should equals " + 
						formatNotEqualString(statement2, index) + "] " + comment);
			}
		}

		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onAssertion(true, statement1, statement2, ComparationType.POSITIVE, comment);
		}
	}

	@Override
	public synchronized void assertNotEquals(Object statement1, Object statement2) {
		assertNotEquals(statement1, statement2, "");
	}

	@Override
	public synchronized void assertNotEquals(Object statement1, Object statement2, String comment) {
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

		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onAssertion(true, statement1, statement2, ComparationType.NEGATIVE, comment);
		}
	}

	@Override
	public synchronized void verifyTrue(Boolean statement1) {
		verifyTrue(statement1, "");
	}

	@Override
	public synchronized void verifyTrue(Boolean statement1, String comment) {
		if (statement1 == null || statement1.equals(Boolean.FALSE)) {
			for (AssertionEventListener assertionListener: listeners) {
				assertionListener.onVerification(false, statement1, ComparationType.POSITIVE, comment);
			}
			throw new RuntimeException("Statement verification failed. [Expected true] " + comment);
		}

		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onVerification(true, statement1, ComparationType.POSITIVE, comment);
		}
	}

	@Override
	public synchronized void verifyFalse(Boolean statement1) {
		verifyFalse(statement1, "");
	}

	@Override
	public synchronized void verifyFalse(Boolean statement1, String comment) {
		if (statement1 == null || statement1.equals(Boolean.TRUE)) {
			for (AssertionEventListener assertionListener: listeners) {
				assertionListener.onVerification(false, statement1, ComparationType.NEGATIVE, comment);
			}
			throw new RuntimeException("Statement verification failed. [Expected false] " + comment);
		}

		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onVerification(true, statement1, ComparationType.NEGATIVE, comment);
		}
	}

	@Override
	public synchronized void verifyEquals(Object statement1, Object statement2) {
		verifyEquals(statement1, statement2, "");
	}

	@Override
	public synchronized void verifyEquals(Object statement1, Object statement2, String comment) {
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

		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onVerification(true, statement1, statement2, ComparationType.POSITIVE, comment);
		}
	}

	@Override
	public synchronized void verifyNotEquals(Object statement1, Object statement2) {
		verifyNotEquals(statement1, statement2, "");
	}

	@Override
	public synchronized void verifyNotEquals(Object statement1, Object statement2, String comment) {
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

		for (AssertionEventListener assertionListener: listeners) {
			assertionListener.onVerification(true, statement1, statement2, ComparationType.NEGATIVE, comment);
		}
	}
}