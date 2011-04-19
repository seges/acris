package sk.seges.sesam.core.test.selenium.runner;

import sk.seges.sesam.test.selenium.AbstractSeleniumTest;

public class SeleniumSuiteRunner {

    public void run(AbstractSeleniumTest test) throws Exception {
    	test.setUp();
       	test.runTests();
        test.tearDown();
    }
}
