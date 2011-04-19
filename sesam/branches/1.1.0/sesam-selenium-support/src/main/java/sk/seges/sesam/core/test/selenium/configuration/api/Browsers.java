package sk.seges.sesam.core.test.selenium.configuration.api;

public enum Browsers {

	FIREFOX("*firefox");
	
	private String browser;
	
	Browsers(String browser) {
		this.browser = browser;
	}
	
	@Override
	public String toString() {
		return browser;
	}
	
}