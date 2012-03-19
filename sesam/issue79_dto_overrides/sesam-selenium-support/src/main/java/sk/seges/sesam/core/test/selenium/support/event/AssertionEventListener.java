package sk.seges.sesam.core.test.selenium.support.event;

public interface AssertionEventListener {

	public enum ComparationType {
		POSITIVE, NEGATIVE;
	}

	void onAssertion(Boolean result, Boolean statement1, ComparationType type, String comment);
	void onAssertion(Boolean result, String statement1, String statement2, ComparationType type, String comment);

	void onVerification(Boolean result, Boolean statement1, ComparationType type, String comment);
	void onVerification(Boolean result, String statement1, String statement2, ComparationType type, String comment);
}