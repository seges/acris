package sk.seges.sesam.core.test.webdriver.report.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import sk.seges.sesam.core.test.selenium.configuration.annotation.Report.HtmlReport.IssueTracker;
import sk.seges.sesam.core.test.selenium.configuration.annotation.ReportSettings.HtmlReportSettings.IssueTrackerSettings;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest.Issue;
import sk.seges.sesam.core.test.selenium.report.model.SeleniumOperation;
import sk.seges.sesam.core.test.webdriver.AbstractWebdriverTest;
import sk.seges.sesam.core.test.webdriver.report.model.api.ReportData;

public class TestCaseResult implements ReportData {

	private Long startTime = 0L;
	private Long endTime = 0L;
	private Long checkpoint = 0L;
	
	private final Class<? extends AbstractWebdriverTest> testCase;
	
	private String testMethod;

	private List<CommandResult> commandResults = new LinkedList<CommandResult>();
		
	private String fileName;
	private final IssueTrackerSettings issueTrackerSettings;
	
	public TestCaseResult(Class<? extends AbstractWebdriverTest> testCase, IssueTrackerSettings issueTrackerSettings) {
		this.testCase = testCase;
		this.issueTrackerSettings = issueTrackerSettings;
	}

	public void setTestMethod(String testMethod) {
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
			boolean result = !getTestCase().getMethod(method).getAnnotation(SeleniumTest.class).issue().value().equals(SeleniumTest.UNDEFINED);
			if (result) {
				return getIssueLink() != null;
			}
			return false;
		} catch (Exception e) {
		}
		return false;
	}

	public String getIssueLink() {
		
		if (issueTrackerSettings == null) {
			return null;
		}
		
		String issueLinkFormat = issueTrackerSettings.getIssueLink();
		
		if (issueLinkFormat == null || issueTrackerSettings.getUrl() == null) {
			return null;
		}
		
		String result = issueLinkFormat.replaceAll(IssueTracker.URL, issueTrackerSettings.getUrl());
		return result.replaceAll(IssueTracker.ISSUE_NUMBER, getIssue().value());
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
	
	private List<CommandResult> getFilteredCommandResults(List<CommandResult> commandResults, SeleniumOperation[] operations) {
		List<CommandResult> filteredResult = new ArrayList<CommandResult>();
		
		for (CommandResult result: commandResults) {
			if (result.isFailure() || isOperationLogged(operations, result.getOperation())) {
				filteredResult.add(result);
			}
		}
		
		return filteredResult;
	}
	
	private boolean isOperationLogged(SeleniumOperation[] operations, SeleniumOperation operation) {
		for (SeleniumOperation op: operations) {
			if (op.equals(operation) || operation.equals(SeleniumOperation.ALL)) {
				return true;
			}
		}
		
		return false;
	}

	public void filterCommandResults(SeleniumOperation[] operations) {
		commandResults = getFilteredCommandResults(commandResults, operations);
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

	public Class<? extends AbstractWebdriverTest> getTestCase() {
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