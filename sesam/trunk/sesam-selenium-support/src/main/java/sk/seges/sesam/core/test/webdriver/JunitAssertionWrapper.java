package sk.seges.sesam.core.test.webdriver;

import org.hamcrest.Matcher;
import org.junit.Assert;
import org.junit.internal.ArrayComparisonFailure;

import sk.seges.sesam.core.test.webdriver.api.Assertion;

public class JunitAssertionWrapper implements Assertion {

	@Override
	public void fail() {
		Assert.fail();
	}

	@Override
	public void fail(String message) {
		Assert.fail(message);
	}

	@Override
	public void assertTrue(String message, boolean condition) {
		Assert.assertTrue(message, condition);
	}

	@Override
	public void assertTrue(boolean condition) {
		assertTrue(null, condition);
	}

	@Override
	public void assertFalse(String message, boolean condition) {
		Assert.assertFalse(message, condition);
	}

	@Override
	public void assertFalse(boolean condition) {
		assertFalse(null, condition);
	}

	@Override
	public void assertEquals(String message, Object expected, Object actual) {
		Assert.assertEquals(message, expected, actual);
	}

	@Override
	public void assertEquals(Object expected, Object actual) {
		assertEquals(null, expected, actual);
	}

	@Override
	public void assertArrayEquals(String message, Object[] expecteds, Object[] actuals) {
		Assert.assertArrayEquals(message, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(Object[] expecteds, Object[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(String message, byte[] expecteds, byte[] actuals) {
		Assert.assertArrayEquals(message, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(byte[] expecteds, byte[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(String message, char[] expecteds, char[] actuals) throws ArrayComparisonFailure {
		Assert.assertArrayEquals(message, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(char[] expecteds, char[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(String message, short[] expecteds, short[] actuals) throws ArrayComparisonFailure {
		Assert.assertArrayEquals(message, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(short[] expecteds, short[] actuals) {
		assertArrayEquals(null, expecteds, actuals);		
	}

	@Override
	public void assertArrayEquals(String message, int[] expecteds, int[] actuals) throws ArrayComparisonFailure {
		Assert.assertArrayEquals(message, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(int[] expecteds, int[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(String message, long[] expecteds, long[] actuals) throws ArrayComparisonFailure {
		Assert.assertArrayEquals(message, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(long[] expecteds, long[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(String message, double[] expecteds, double[] actuals, double delta) throws ArrayComparisonFailure {
		Assert.assertArrayEquals(message, expecteds, actuals, delta);
	}

	@Override
	public void assertArrayEquals(double[] expecteds, double[] actuals, double delta) {
		assertArrayEquals(null, expecteds, actuals, delta);
	}

	@Override
	public void assertArrayEquals(String message, float[] expecteds, float[] actuals, float delta) throws ArrayComparisonFailure {
		Assert.assertArrayEquals(message, expecteds, actuals, delta);
	}

	@Override
	public void assertArrayEquals(float[] expecteds, float[] actuals, float delta) {
		assertArrayEquals(null, expecteds, actuals, delta);
	}

	@Override
	public void assertEquals(String message, double expected, double actual) {
		Assert.assertEquals(message, expected, actual);
	}

	@Override
	public void assertEquals(long expected, long actual) {
		assertEquals(null, expected, actual);
	}

	@Override
	public void assertEquals(String message, long expected, long actual) {
		Assert.assertEquals(message, expected, actual);
	}

	@Override
	public void assertEquals(double expected, double actual, double delta) {
		Assert.assertEquals(null, expected, actual, delta);
	}

	@Override
	public void assertNotNull(String message, Object object) {
		Assert.assertNotNull(message, object);
	}

	@Override
	public void assertNotNull(Object object) {
		assertNotNull(null, object);
	}

	@Override
	public void assertNull(String message, Object object) {
		Assert.assertNull(message, object);
	}

	@Override
	public void assertNull(Object object) {
		assertNull(object);
	}

	@Override
	public void assertSame(String message, Object expected, Object actual) {
		Assert.assertSame(message, expected, actual);
	}

	@Override
	public void assertSame(Object expected, Object actual) {
		assertSame(null, expected, actual);
	}

	@Override
	public void assertNotSame(String message, Object unexpected, Object actual) {
		Assert.assertNotSame(message, unexpected, actual);
	}

	@Override
	public void assertNotSame(Object unexpected, Object actual) {
		assertNotSame(null, unexpected, actual);
	}

	@Override
	public <T> void assertThat(T actual, Matcher<T> matcher) {
		assertThat(null, actual, matcher);
	}

	@Override
	public <T> void assertThat(String reason, T actual, Matcher<T> matcher) {
		Assert.assertThat(reason, actual, matcher);
	}
}