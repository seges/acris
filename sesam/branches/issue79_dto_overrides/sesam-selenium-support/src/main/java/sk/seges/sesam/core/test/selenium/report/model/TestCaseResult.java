package sk.seges.sesam.core.test.selenium.report.model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest.Issue;
import sk.seges.sesam.core.test.selenium.report.model.api.ReportData;

public class TestCaseResult implements ReportData {

	private Long startTime = 0L;
	private Long endTime = 0L;
	private Long checkpoint = 0L;
	
	private final Class<? extends AbstractSeleniumTest> testCase;
	private final String testMethod;

	private List<CommandResult> commandResults = new LinkedList<CommandResult>();
		
	private String fileName;
	
	public TestCaseResult(Class<? extends AbstractSeleniumTest> testCase, String testMethod) {
		this.testCase = testCase;
		this.testMethod = testMethod;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public boolean hasBugReported() {
		try {
			String method = getTestMethod();
			return !getTestCase().getMethod(method).getAnnotation(SeleniumTest.class).issue().value().equals(SeleniumTest.UNDEFINED);
		} catch (Exception e) {
		}
		return false;
	}

	public String getIssueLink() {
		return "https://local.seges.sk/mantis/view.php?id=" + getIssue().value();
	}
	
	public Issue getIssue() {
		try {
			String method = getTestMethod();
			return getTestCase().getMethod(method).getAnnotation(SeleniumTest.class).issue();
		} catch (Exception e) {
		}
		
		return null;
	}
	
	public SeleniumOperationResult getStatus() {
		for (CommandResult commandResult: commandResults) {
			if (commandResult.isFailure() || commandResult.getState().equals(SeleniumOperationResult.FAILURE)) {
				return SeleniumOperationResult.FAILURE;
			}
		}
		
		return SeleniumOperationResult.OK;
	}
	
	public List<CommandResult> getCommandResults() {
		return commandResults;
	}
	
	public String getStartDate() {
		return new SimpleDateFormat().format(new Date(startTime));
	}
	
	public String getTestDescription() {
		try {
			return getTestCase().getMethod(getTestMethod()).getAnnotation(SeleniumTest.class).description();
		} catch (Exception e) {
			return "Description is missing";
		}
	}

	public String getTestMethod() {
		return testMethod;
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
	
	public Long getTotalSeconds() {
		return getTotalTime() / 1000;
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