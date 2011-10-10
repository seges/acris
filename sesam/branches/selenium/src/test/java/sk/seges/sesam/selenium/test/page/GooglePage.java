package sk.seges.sesam.selenium.test.page;

import org.openqa.selenium.By;

public class GooglePage {

	public By getSearchBoxLocator() {
		return By.xpath("//input[@type='text']");
	}
	
	public By getSearchButton() {
		return By.xpath("//input[@type='submit' and text()='Google Search']");
	}

	public By getResultLink() {
		return By.xpath("//a[contains(@href, 'www.seges.')]");
	}
}