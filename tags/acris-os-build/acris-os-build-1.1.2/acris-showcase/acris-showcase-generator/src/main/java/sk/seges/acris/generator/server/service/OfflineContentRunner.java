package sk.seges.acris.generator.server.service;

import junit.framework.TestSuite;
import junit.textui.TestRunner;
import sk.seges.acris.generator.client.GwtTestGenerateOfflineContent;
import sk.seges.acris.generator.offline.OfflineGenerator;

import com.google.gwt.junit.TestResultsCleaner;


public class OfflineContentRunner {

	public void startGenerator() {

		try {
			TestResultsCleaner singleJUnitShell = new TestResultsCleaner();
			singleJUnitShell.clearUnitResult();

			TestSuite suite = new TestSuite();
	
			GwtTestGenerateOfflineContent generateOfflineContent = new OfflineGenerator();
			generateOfflineContent.setName("testLoadContent");
			
			TestRunner aTestRunner = new TestRunner();
			suite.addTest(generateOfflineContent);
			aTestRunner.doRun(suite, false);
		} catch (Throwable th) {
			System.out.println(th);
		}
	}
	
	public static void main(String[] args) {
		new OfflineContentRunner().startGenerator();
	}
}
