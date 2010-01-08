package com.google.gwt.junit;

import java.util.List;

import com.google.gwt.junit.client.impl.JUnitHost.TestInfo;

public class TestResultsCleaner {
	
	public void clearUnitResult() {

		List<TestInfo[]> testInfos = JUnitShell.getMessageQueue().getTestBlocks();

		for (TestInfo[] testInfoList : testInfos) {
			for (TestInfo testInfo : testInfoList) {
				JUnitShell.getMessageQueue().removeResults(testInfo);
			}
		}
	}
}
