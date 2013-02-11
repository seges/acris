package sk.seges.sesam.core.test.webdriver.api;

import org.hamcrest.Matcher;
import org.junit.internal.ArrayComparisonFailure;

public interface Assertion {

	void fail();
	
	void fail(String message);
	
	void assertTrue(String message, boolean condition);

	void assertTrue(boolean condition);

	void assertFalse(String message, boolean condition);

	void assertFalse(boolean condition);

	void assertEquals(String message, Object expected, Object actual);

	void assertEquals(Object expected, Object actual);

	void assertArrayEquals(String message, Object[] expecteds, Object[] actuals);

	void assertArrayEquals(Object[] expecteds, Object[] actuals);

	void assertArrayEquals(String message, byte[] expecteds, byte[] actuals);

	void assertArrayEquals(byte[] expecteds, byte[] actuals);

	void assertArrayEquals(String message, char[] expecteds, char[] actuals) throws ArrayComparisonFailure;

	void assertArrayEquals(char[] expecteds, char[] actuals);

	void assertArrayEquals(String message, short[] expecteds, short[] actuals) throws ArrayComparisonFailure;

	void assertArrayEquals(short[] expecteds, short[] actuals);

	void assertArrayEquals(String message, int[] expecteds, int[] actuals) throws ArrayComparisonFailure;

	void assertArrayEquals(int[] expecteds, int[] actuals);

	void assertArrayEquals(String message, long[] expecteds, long[] actuals) throws ArrayComparisonFailure;

	void assertArrayEquals(long[] expecteds, long[] actuals);

	void assertArrayEquals(String message, double[] expecteds, double[] actuals, double delta) throws ArrayComparisonFailure;

	void assertArrayEquals(double[] expecteds, double[] actuals, double delta);

	void assertArrayEquals(String message, float[] expecteds, float[] actuals, float delta) throws ArrayComparisonFailure;

	void assertArrayEquals(float[] expecteds, float[] actuals, float delta);

	void assertEquals(String message, double expected, double actual);

	void assertEquals(long expected, long actual);

	void assertEquals(String message, long expected, long actual);

	void assertEquals(double expected, double actual, double delta);

	void assertNotNull(String message, Object object);

	void assertNotNull(Object object);

	void assertNull(String message, Object object);

	void assertNull(Object object);

	void assertSame(String message, Object expected, Object actual);

	void assertSame(Object expected, Object actual);

	void assertNotSame(String message, Object unexpected, Object actual);

	void assertNotSame(Object unexpected, Object actual);

	<T> void assertThat(T actual, Matcher<T> matcher);

	<T> void assertThat(String reason, T actual, Matcher<T> matcher);
}