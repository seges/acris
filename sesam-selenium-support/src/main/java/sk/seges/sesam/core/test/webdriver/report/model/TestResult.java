package sk.seges.sesam.core.test.webdriver.report.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import sk.seges.sesam.core.test.webdriver.report.model.api.ReportData;

public class TestResult implements ReportData {

	private static final String RESOURCE_BASE_DIR = "http://acris.googlecode.com/svn/sesam/trunk/sesam-selenium-support/src/main/resources/sk/seges/sesam/webdriver/report/metal/images/";
	
	private Map<String, TestCaseCollectionResult> testCaseCollections = new HashMap<String, TestCaseCollectionResult>();
	
	private String fileName;
	
	public Collection<TestCaseCollectionResult> getTestCaseCollectionResults() {
		return testCaseCollections.values();
	}

	public int getTestCasesCount() {
		int count = 0;
		for (TestCaseCollectionResult testCaseCollectionResult: getTestCaseCollectionResults()) {
			count += testCaseCollectionResult.getTestCaseResults().size();
		}
		
		return count;
	}
	
	public int getFailedTestCasesCount() {
		int count = 0;
		for (TestCaseCollectionResult testCaseCollectionResult: getTestCaseCollectionResults()) {
			count += testCaseCollectionResult.getFailedTestCaseResults().size();
		}
		
		return count;
	}
	
	public String getResourceDir() {
		return RESOURCE_BASE_DIR;
	}
	
	public void addTestCaseResult(TestCaseResult testCaseResult) {
		String testName = testCaseResult.getTestCase().getSimpleName();
		
		TestCaseCollectionResult testCaseCollectionResult = testCaseCollections.get(testName);
		
		if (testCaseCollectionResult == null) {
			testCaseCollectionResult = new TestCaseCollectionResult(testCaseResult.getTestCase());
			testCaseCollections.put(testName, testCaseCollectionResult);
		}
		
		testCaseCollectionResult.addTestCaseResult(testCaseResult);
	}

	@Override
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public String getFileName() {
		return fileName;
	}
}