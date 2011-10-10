package sk.seges.sesam.selenium.test.cases;

import org.junit.Test;

import sk.seges.sesam.core.test.selenium.AbstractSeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.annotation.SeleniumTest;
import sk.seges.sesam.core.test.selenium.configuration.model.CoreSeleniumSettingsProvider;
import sk.seges.sesam.selenium.SuiteRunner;
import sk.seges.sesam.selenium.test.page.GooglePage;

@SeleniumTest(suiteRunner = SuiteRunner.class)
public class GoogleSearchTestCase extends AbstractSeleniumTest {

	@Override
	protected CoreSeleniumSettingsProvider getSettings() {
		return new GoogleSearchTestCaseConfiguration();
	}

	@Test
	public void searchTest() {
		GooglePage googlePage = new GooglePage();
		webDriver.findElement(googlePage.getSearchBoxLocator()).sendKeys("seges");
		webDriver.findElement(googlePage.getResultLink());
	}
}
