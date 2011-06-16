package com.google.gwt.junit;

import java.lang.reflect.Field;
import java.util.List;

import com.google.gwt.junit.client.impl.JUnitHost.TestInfo;

public class TestResultsCleaner {
	
	public void clearUnitResult() {

		try {
			Field unitTestShellField  = JUnitShell.class.getDeclaredField("unitTestShell");
			unitTestShellField.setAccessible(true);
			JUnitShell unitShell = (JUnitShell)unitTestShellField.get(null);
			//If the unit shell is still not created, do nothing
			//This could be done when the test case is run first time
			if (unitShell == null) {
				return;
			}
			Field lastLaunchFailedField = JUnitShell.class.getDeclaredField("lastLaunchFailed");
			lastLaunchFailedField.setAccessible(true);
			
			lastLaunchFailedField.setBoolean(unitShell, false);
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		}
		
		List<TestInfo[]> testInfos = JUnitShell.getMessageQueue().getTestBlocks();

		//Removing previous results in order to run the test again
		for (TestInfo[] testInfoList : testInfos) {
			for (TestInfo testInfo : testInfoList) {
				JUnitShell.getMessageQueue().removeResults(testInfo);
			}
		}
	}
}
