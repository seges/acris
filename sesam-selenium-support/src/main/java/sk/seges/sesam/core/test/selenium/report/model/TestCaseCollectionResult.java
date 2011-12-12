package sk.seges.sesam.core.test.selenium.report.model;

import java.util.ArrayList;
import java.util.List;

public class TestCaseCollectionResult {

	private List<TestCaseResult> testCases = new ArrayList<TestCaseResult>();

	private String testName;

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
	
	public TestCaseCollectionResult(String simpleTestName) {
		this.testName = simpleTestName;
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