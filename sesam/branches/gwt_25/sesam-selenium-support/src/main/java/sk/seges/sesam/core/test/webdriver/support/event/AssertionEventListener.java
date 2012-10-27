package sk.seges.sesam.core.test.webdriver.support.event;

public interface AssertionEventListener {

	public enum ComparationType {
		POSITIVE, NEGATIVE;
	}

	void onAssertion(Boolean result, Boolean statement1, ComparationType type, String comment);
	void onAssertion(Boolean result, Object statement1, Object statement2, ComparationType type, String comment);

	void onVerification(Boolean result, Boolean statement1, ComparationType type, String comment);
	void onVerification(Boolean result, Object statement1, Object statement2, ComparationType type, String comment);
}