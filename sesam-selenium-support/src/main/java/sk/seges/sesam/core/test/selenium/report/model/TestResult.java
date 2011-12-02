package sk.seges.sesam.core.test.selenium.report.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;

public class TestResult {

	private Long startTime = 0L;
	private Long endTime = 0L;
	private Long checkpoint = 0L;
	
	private final Class<? extends AbstractSeleniumTest> testCase;

	private List<CommandResult> commandResults = new LinkedList<CommandResult>();
	
	public TestResult(Class<? extends AbstractSeleniumTest> testCase) {
		this.testCase = testCase;
	}

	public List<CommandResult> getCommandResults() {
		return commandResults;
	}
	
	public String getStartDate() {
		return new SimpleDateFormat().format(new Date(startTime));
	}
	
	public String getTestMethod() {
		StackTraceElement stackTraceElement = getStackTraceElement();
		
		if (stackTraceElement != null) {
			return stackTraceElement.getMethodName();
		}
		
		return null;
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

	
	public Class<? extends AbstractSeleniumTest> getTestCase() {
		return testCase;
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
	
}