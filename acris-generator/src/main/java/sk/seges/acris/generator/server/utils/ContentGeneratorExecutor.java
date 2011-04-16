package sk.seges.acris.generator.server.utils;

import junit.framework.TestSuite;
import junit.textui.TestRunner;
import sk.seges.acris.generator.client.GwtTestGenerateOfflineContent;

import com.google.gwt.junit.TestResultsCleaner;

public abstract class ContentGeneratorExecutor {

	protected abstract GwtTestGenerateOfflineContent getGwtOfflineTest();
	
	protected String getTestMethod() {
		return "testLoadContent";
	}
	
	public void startOfflineProcessing() {

		try {
			TestResultsCleaner singleJUnitShell = new TestResultsCleaner();
			singleJUnitShell.clearUnitResult();
			
			TestSuite suite = new TestSuite();
	
			GwtTestGenerateOfflineContent generateOfflineContent = getGwtOfflineTest();
			generateOfflineContent.setName(getTestMethod());
			
			TestRunner aTestRunner = new TestRunner();
			suite.addTest(generateOfflineContent);
			aTestRunner.doRun(suite, false);
						
		} catch (Throwable th) {
			System.out.println(th);
			System.exit(1);
		} finally {
			//Trust me, this is really important!
			System.exit(0);
		}
	}

}
