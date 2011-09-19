package sk.seges.sesam.core.test.selenium.report.model;

import java.util.Date;

import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;

public class TestInfo {

	private Long startTime = 0L;
	private Long endTime = 0L;
	private Long checkpoint = 0L;
	
	private final Class<? extends AbstractSeleniumTest> testCase;

	private SeleniumOperation operation;
	private SeleniumOperationResult result;
	private SeleniumOperationState state;
	
	private Object[] parameters;
	private Throwable throwable;
	
	public TestInfo(Class<? extends AbstractSeleniumTest> testCase) {
		this.testCase = testCase;
	}

	public Class<? extends AbstractSeleniumTest> getTestCase() {
		return testCase;
	}
	
	public void setThrowable(Throwable throwable) {
		this.throwable = throwable;
	}
	
	public Throwable getThrowable() {
		return throwable;
	}
	
	public void setOperation(SeleniumOperation operation) {
		this.operation = operation;
	}
	
	public SeleniumOperation getOperation() {
		return operation;
	}
	
	public void setResult(SeleniumOperationResult result) {
		this.result = result;
	}
	
	public SeleniumOperationResult getResult() {
		return result;
	}
	
	public void setState(SeleniumOperationState state) {
		this.state = state;
	}
	
	public SeleniumOperationState getState() {
		return state;
	}
	
	public void startTest() {
		if (!startTime.equals(0L)) {
			throw new RuntimeException("Test was not already started!");
		}
		this.startTime = new Date().getTime();
		this.checkpoint = this.startTime;
	}
	
	public void endTest() {
		if (startTime.equals(0L)) {
			throw new RuntimeException("Test was not started yet!");
		}
		if (!endTime.equals(0L)) {
			throw new RuntimeException("Test was not already finished!");
		}
		this.endTime = new Date().getTime();
	}
	
	public Long getDuration() {
		if (startTime.equals(0L)) {
			throw new RuntimeException("Test was not started yet!");
		}
		return new Date().getTime() - startTime;
	}

	public Long getLastOperationDuration() {
		if (startTime.equals(0L)) {
			throw new RuntimeException("Test was not started yet!");
		}
		Long time = new Date().getTime();
		
		Long result = time - checkpoint;
		this.checkpoint = time;
		return result;
	}
	
	public Long getTotalTime() {
		if (startTime.equals(0L)) {
			throw new RuntimeException("Test was not started yet!");
		}
		if (endTime.equals(0L)) {
			throw new RuntimeException("Test was not finished yet!");
		}
		return endTime - startTime;
	}
	
	private StackTraceElement getStackTraceElement() {
		StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();

		for (int i = 0; i < stackTrace.length; i++) {
			if (stackTrace[i].getClassName().equals(testCase.getCanonicalName())) {
				return stackTrace[i];
			}
		}
		
		return null;
	}
	
	public int getLineNumber() {
		StackTraceElement stackTraceElement = getStackTraceElement();
		
		if (stackTraceElement != null) {
			return stackTraceElement.getLineNumber();
		}
		throw new RuntimeException("Unable to identify line number for the test " + testCase.getCanonicalName());
	}

	public String getTestMethod() {
		StackTraceElement stackTraceElement = getStackTraceElement();
		
		if (stackTraceElement != null) {
			return stackTraceElement.getMethodName();
		}
		throw new RuntimeException("Unable to identify method for the test " + testCase.getCanonicalName());
	}
	
	public void setParameters(Object... parameters) {
		this.parameters = parameters;
	}
	
	public Object[] getParameters() {
		return parameters;
	}
}