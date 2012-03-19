package sk.seges.sesam.core.test.selenium.report.model;

import java.util.ArrayList;
import java.util.List;

import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTestCase;

public class TestCaseCollectionResult {

	private List<TestCaseResult> testCases = new ArrayList<TestCaseResult>();

	private String testName;
	private Class<? extends AbstractSeleniumTest> seleniumTestClass;

	private enum ResultsFilter {
		
		FAILURE_STATE {
			@Override
			public boolean isValid(TestCaseResult result) {
				return result.getStatus().equals(SeleniumOperationResult.FAILURE);
			}
		};
		
		public abstract boolean isValid(TestCaseResult result);
	}

	private List<TestCaseResult> getFilteredResult(List<TestCaseResult> results, ResultsFilter filter) {
		List<TestCaseResult> filteredResult = new ArrayList<TestCaseResult>();
		
		for (TestCaseResult result: results) {
			if (filter.isValid(result)) {
				filteredResult.add(result);
			}
		}
		
		return filteredResult;
	}
	
	public TestCaseCollectionResult(Class<? extends AbstractSeleniumTest> seleniumTestClass) {
		this.testName = seleniumTestClass.getSimpleName();
		this.seleniumTestClass = seleniumTestClass;
	}

	public String getDescription() {
		try {
			return seleniumTestClass.getAnnotation(SeleniumTestCase.class).description();
		} catch (Exception e) {
			return "Description is missing";
		}
	}

	public String getTestName() {
		return testName;
	}
	
	public List<TestCaseResult> getTestCaseResults() {
		return testCases;
	}

	public List<TestCaseResult> getFailedTestCaseResults() {
		return getFilteredResult(getTestCaseResults(), ResultsFilter.FAILURE_STATE);
	}
	
	public void addTestCaseResult(TestCaseResult testCaseResult) {
		testCases.add(testCaseResult);
	}
}