package sk.seges.sesam.core.test.selenium.configuration.api;

public enum Browsers {

	FIREFOX("*firefox"),
	IE("*iexplore"),
	SAFARI("*safari"),
	OPERA("*opera"),
	CHROME("*chrome");
	
	private String browser;
	
	Browsers(String browser) {
		this.browser = browser;
	}
	
	@Override
	public String toString() {
		return browser;
	}
	
}