package sk.seges.acris.generator.server.utils;

import com.google.gwt.junit.TestResultsCleaner;
import junit.framework.TestSuite;
import junit.textui.TestRunner;
import org.cyberneko.html.HTMLScanner;
import sk.seges.acris.generator.client.GwtTestGenerateOfflineContent;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public abstract class ContentGeneratorExecutor {

	protected abstract GwtTestGenerateOfflineContent getGwtOfflineTest();

	protected String getTestMethod() {
		return "testLoadContent";
	}

	public void setHtmlProcessingDefaults() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
		for (Field field: HTMLScanner.class.getDeclaredFields()) {
			if (field.getName().equals("RECOGNIZED_FEATURES_DEFAULTS")) {
				field.setAccessible(true);

				Field modifiersField = Field.class.getDeclaredField("modifiers");
			    modifiersField.setAccessible(true);
			    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			    
				field.set(null, new Boolean[] {
				        null,
				        null,
				        Boolean.TRUE,
				        Boolean.FALSE,
				        Boolean.FALSE,
				        Boolean.FALSE,
				        Boolean.FALSE,
				        Boolean.FALSE,
				        Boolean.FALSE,
				        Boolean.FALSE,
				        Boolean.FALSE,
				        Boolean.FALSE,
				        Boolean.FALSE,
				        Boolean.FALSE,
				        Boolean.FALSE,
				        Boolean.TRUE,
				});
			}
		}	
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
			
//			setHtmlProcessingDefaults();
            System.out.println("Starting test...");

            aTestRunner.doRun(suite, false);

            System.out.println("Done");

        } catch (Throwable th) {
			System.out.println(th);
			System.exit(1);
		} finally {
			//Trust me, this is really important!
			System.exit(0);
		}
	}
}