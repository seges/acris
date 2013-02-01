package sk.seges.sesam.core.test.webdriver.report.model.api;

public enum TemplateLocation {
	FILE("file"), CLASSPATH("classpath");
	
	String loader;
	
	TemplateLocation(String loader) {
		this.loader = loader;
	}
	
	public String getLoader() {
		return loader;
	}
}