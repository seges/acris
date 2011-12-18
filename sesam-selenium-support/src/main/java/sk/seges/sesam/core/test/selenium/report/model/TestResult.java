package sk.seges.sesam.core.test.selenium.report.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import sk.seges.sesam.core.test.selenium.report.model.api.ReportData;

public class TestResult implements ReportData {

	private static final String RESOURCE_BASE_DIR = "http://acris.googlecode.com/svn/sesam/trunk/sesam-selenium-support/src/main/resources/sk/seges/sesam/selenium/report/metal/images/";
	
	private Map<String, TestCaseCollectionResult> testCaseCollections = new HashMap<String, TestCaseCollectionResult>();
	
	private String fileName;
	
	public Collection<TestCaseCollectionResult> getTestCaseCollectionResults() {
		return testCaseCollections.values();
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
