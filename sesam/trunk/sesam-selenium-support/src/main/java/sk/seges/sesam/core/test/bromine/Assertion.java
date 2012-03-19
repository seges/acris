package sk.seges.sesam.core.test.bromine;

public interface Assertion {

	void assertTrue(Boolean statement1);

	void assertTrue(Boolean statement1, String comment);

	void assertFalse(Boolean statement1);

	void assertFalse(Boolean statement1, String comment);

	void assertEquals(String statement1, String statement2);

	void assertEquals(String statement1, String statement2, String comment);

	void assertNotEquals(String statement1, String statement2);

	void assertNotEquals(String statement1, String statement2, String comment);

	void verifyTrue(Boolean statement1);

	void verifyTrue(Boolean statement1, String comment);

	void verifyFalse(Boolean statement1);

	void verifyFalse(Boolean statement1, String comment);

	void verifyEquals(String statement1, String statement2);

	void verifyEquals(String statement1, String statement2, String comment);

	void verifyNotEquals(String statement1, String statement2);

	void verifyNotEquals(String statement1, String statement2, String comment);
}