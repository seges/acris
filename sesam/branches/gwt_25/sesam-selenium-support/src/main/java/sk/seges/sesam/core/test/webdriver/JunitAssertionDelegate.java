package sk.seges.sesam.core.test.webdriver;

import org.hamcrest.Matcher;
import org.junit.internal.ArrayComparisonFailure;

import sk.seges.sesam.core.test.webdriver.api.Assertion;

public class JunitAssertionDelegate implements Assertion {

	enum AssertionType {
		EQUALS, SAME, NOT_SAME, NULL, NOT_NULL, THAT;
	}
	
	public class AssertionResult {
		
		private double delta;
		
		private Boolean result;
		private String resultMessage;
		
		private Object[] expecteds;
		private Object[] actuals;
		private String comment;

		private Boolean expectation;
	
		private Object expected;
		private Object actual;
		
		private final AssertionType assertionType;
		
		public AssertionResult(AssertionType assertionType, Object[] expecteds, Object[] actuals, String comment) {
			this.expecteds = expecteds;
			this.comment = comment;
			this.actuals = actuals;
			this.assertionType = assertionType;
		}
		
		public AssertionResult(AssertionType assertionType, Object[] expecteds, Object[] actuals, String comment, double delta) {
			this(assertionType, expecteds, actuals, comment);
			this.delta = delta;
		}
		
		public AssertionResult(AssertionType assertionType, Object expected, Object actual, String comment) {
			this.expected = expected;
			this.comment = comment;
			this.actual = actual;
			this.assertionType = assertionType;
		}
		
		public AssertionResult(AssertionType assertionType, Boolean expected, Boolean actual, String comment) {
			this.expected = expected;
			this.actual = actual;
			this.comment = comment;
			this.assertionType = assertionType;
		}
		
		public void setResult(Boolean result) {
			this.result = result;
		}

		public void setResultMessage(String resultMessage) {
			this.resultMessage = resultMessage;
		}
		
		public Boolean getResult() {
			return result;
		}

		public Object[] getExpecteds() {
			return expecteds;
		}

		public Object[] getActuals() {
			return actuals;
		}

		public String getComment() {
			return comment;
		}

		public Boolean getExpectation() {
			return expectation;
		}

		public Object getExpected() {
			return expected;
		}

		public Object getActual() {
			return actual;
		}
		
		public double getDelta() {
			return delta;
		}
		
		public AssertionType getAssertionType() {
			return assertionType;
		}
		
		public String getResultMessage() {
			return resultMessage;
		}
	}
	
	private final JunitAssertionWrapper assertion;
	
	public JunitAssertionDelegate(JunitAssertionWrapper assertion) {
		this.assertion = assertion;
	}

	protected void beforeFail(String message) {};
	
	protected void afterFail(String message) {};
	
	protected AssertionResult beforeAssert(AssertionResult result) {
		return result;
	};
	
	protected void afterAssert(AssertionResult assertionResult) {};
	
	private Object[] toObjectsArray(float[] vals) {
		Object[] result = new Object[vals.length];
		for (int i = 0; i < vals.length; i++) {
			vals[i] = new Float(vals[i]);
		}
		
		return result;
	}

	private Object[] toObjectsArray(long[] vals) {
		Object[] result = new Object[vals.length];
		for (int i = 0; i < vals.length; i++) {
			vals[i] = new Long(vals[i]);
		}
		
		return result;
	}

	private Object[] toObjectsArray(double[] vals) {
		Object[] result = new Object[vals.length];
		for (int i = 0; i < vals.length; i++) {
			vals[i] = new Double(vals[i]);
		}
		
		return result;
	}

	private Object[] toObjectsArray(char[] vals) {
		Object[] result = new Object[vals.length];
		for (int i = 0; i < vals.length; i++) {
			vals[i] = new Character(vals[i]);
		}
		
		return result;
	}

	private Object[] toObjectsArray(int[] vals) {
		Object[] result = new Object[vals.length];
		for (int i = 0; i < vals.length; i++) {
			vals[i] = new Integer(vals[i]);
		}
		
		return result;
	}

	private Object[] toObjectsArray(byte[] vals) {
		Object[] result = new Object[vals.length];
		for (int i = 0; i < vals.length; i++) {
			vals[i] = new Byte(vals[i]);
		}
		
		return result;
	}

	private Object[] toObjectsArray(short[] vals) {
		Object[] result = new Object[vals.length];
		for (int i = 0; i < vals.length; i++) {
			vals[i] = new Short(vals[i]);
		}
		
		return result;
	}

	
	@Override
	public void fail() {
		assertion.fail(null);
	}

	@Override
	public void fail(String message) {
		beforeFail(message);
		assertion.fail(message);
		afterFail(message);
	}

	@Override
	public void assertTrue(String message, boolean condition) {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.EQUALS, true, condition, message));
		try {
			assertion.assertTrue(message, condition);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertTrue(boolean condition) {
		assertTrue(null, condition);
	}

	@Override
	public void assertFalse(String message, boolean condition) {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.EQUALS, false, condition, message));
		try {
			assertion.assertTrue(message, condition);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertFalse(boolean condition) {
		assertion.assertFalse(null, condition);
	}

	@Override
	public void assertEquals(String message, Object expected, Object actual) {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.EQUALS, expected, actual, message));
		try {
			assertion.assertEquals(message, expected, actual);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertEquals(Object expected, Object actual) {
		assertEquals(null, expected, actual);
	}

	@Override
	public void assertArrayEquals(String message, Object[] expecteds, Object[] actuals) {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.EQUALS, expecteds, actuals, message));
		try {
			assertion.assertArrayEquals(message, expecteds, actuals);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertArrayEquals(Object[] expecteds, Object[] actuals) {
		assertArrayEquals(expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(String message, byte[] expecteds, byte[] actuals) {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.EQUALS, toObjectsArray(expecteds), toObjectsArray(actuals), message));
		try {
			assertion.assertArrayEquals(message, expecteds, actuals);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertArrayEquals(byte[] expecteds, byte[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(String message, char[] expecteds, char[] actuals) throws ArrayComparisonFailure {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.EQUALS, toObjectsArray(expecteds), toObjectsArray(actuals), message));
		try {
			assertion.assertArrayEquals(message, expecteds, actuals);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertArrayEquals(char[] expecteds, char[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(String message, short[] expecteds, short[] actuals) throws ArrayComparisonFailure {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.EQUALS, toObjectsArray(expecteds), toObjectsArray(actuals), message));
		try {
			assertion.assertArrayEquals(message, expecteds, actuals);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertArrayEquals(short[] expecteds, short[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(String message, int[] expecteds, int[] actuals) throws ArrayComparisonFailure {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.EQUALS, toObjectsArray(expecteds), toObjectsArray(actuals), message));
		try {
			assertion.assertArrayEquals(message, expecteds, actuals);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertArrayEquals(int[] expecteds, int[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(String message, long[] expecteds, long[] actuals) throws ArrayComparisonFailure {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.EQUALS, toObjectsArray(expecteds), toObjectsArray(actuals), message));
		try {
			assertion.assertArrayEquals(message, expecteds, actuals);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertArrayEquals(long[] expecteds, long[] actuals) {
		assertArrayEquals(null, expecteds, actuals);
	}

	@Override
	public void assertArrayEquals(String message, double[] expecteds, double[] actuals, double delta) throws ArrayComparisonFailure {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.EQUALS, toObjectsArray(expecteds), toObjectsArray(actuals), message, delta));
		try {
			assertion.assertArrayEquals(message, expecteds, actuals, delta);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertArrayEquals(double[] expecteds, double[] actuals, double delta) {
		assertArrayEquals(null, expecteds, actuals, delta);
	}

	@Override
	public void assertArrayEquals(String message, float[] expecteds, float[] actuals, float delta) throws ArrayComparisonFailure {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.EQUALS, toObjectsArray(expecteds), toObjectsArray(actuals), message, delta));
		try {
			assertion.assertArrayEquals(message, expecteds, actuals, delta);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertArrayEquals(float[] expecteds, float[] actuals, float delta) {
		assertArrayEquals(null, expecteds, actuals, delta);
	}

	@Override
	public void assertEquals(String message, double expected, double actual) {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.EQUALS, expected, actual, message));
		try {
			assertion.assertEquals(message, expected, actual);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertEquals(long expected, long actual) {
		assertEquals(null, expected, actual);
	}

	@Override
	public void assertEquals(String message, long expected, long actual) {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.EQUALS, expected, actual, message));
		try {
			assertion.assertEquals(message, expected, actual);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertEquals(double expected, double actual, double delta) {
		assertEquals(null, expected, actual);
	}

	@Override
	public void assertNotNull(String message, Object object) {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.NOT_NULL, null, object, message));
		try {
			assertion.assertNotNull(message, object);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertNotNull(Object object) {
		assertNotNull(null, object);
	}

	@Override
	public void assertNull(String message, Object object) {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.NULL, null, object, message));
		try {
			assertion.assertNull(message, object);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertNull(Object object) {
		assertNull(null, object);
	}

	@Override
	public void assertSame(String message, Object expected, Object actual) {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.SAME, expected, actual, message));
		try {
			assertion.assertSame(message, expected, actual);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}

	@Override
	public void assertSame(Object expected, Object actual) {
		assertSame(null, expected, actual);
	}

	@Override
	public void assertNotSame(String message, Object unexpected, Object actual) {
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.NOT_SAME, unexpected, actual, message));
		try {
			assertion.assertNotSame(message, unexpected, actual);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
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
		AssertionResult assertionResult = beforeAssert(new AssertionResult(AssertionType.THAT, actual, null, reason));
		try {
			assertion.assertThat(reason, actual, matcher);
			assertionResult.setResult(true);
			afterAssert(assertionResult);
		} catch (AssertionError e) {
			assertionResult.setResult(false);
			assertionResult.setResultMessage(e.getMessage());
			afterAssert(assertionResult);
			throw e;
		}
	}	
}