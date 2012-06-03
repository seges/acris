package sk.seges.sesam.core.test.webdriver.api;

public interface Assertion {

	void assertTrue(Boolean statement1);
	void assertTrue(Boolean statement1, String comment);

	void assertFalse(Boolean statement1);
	void assertFalse(Boolean statement1, String comment);
	
	void assertEquals(Object statement1, Object statement2);
	void assertEquals(Object statement1, Object statement2, String comment);

	void assertNotEquals(Object statement1, Object statement2);
	void assertNotEquals(Object statement1, Object statement2, String comment);

	void verifyTrue(Boolean statement1);
	void verifyTrue(Boolean statement1, String comment);

	void verifyFalse(Boolean statement1);
	void verifyFalse(Boolean statement1, String comment);

	void verifyEquals(Object statement1, Object statement2);
	void verifyEquals(Object statement1, Object statement2, String comment);

	void verifyNotEquals(Object statement1, Object statement2);
	void verifyNotEquals(Object statement1, Object statement2, String comment);
}