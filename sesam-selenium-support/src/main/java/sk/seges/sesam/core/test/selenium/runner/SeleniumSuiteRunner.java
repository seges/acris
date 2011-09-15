package sk.seges.sesam.core.test.selenium.runner;

import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;

public class SeleniumSuiteRunner {

	protected SeleniumSuiteRunner() {
	}
	
    public void run(AbstractSeleniumTest test) {
    	test.setUp();
       	test.runTests();
        test.tearDown();
    }
}