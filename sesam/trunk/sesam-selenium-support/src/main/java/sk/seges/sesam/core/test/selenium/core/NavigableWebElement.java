package sk.seges.sesam.core.test.selenium.core;

import org.openqa.selenium.WebElement;

public interface NavigableWebElement<T extends NavigablePage> extends WebElement {

	void navigate();
	
	T getNavigablePage();
	
	WebElement getWebElement();
}