package sk.seges.sesam.pap.test.selenium.processor.printer.api;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumTestTypeElement;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSettingsContext;

public interface SeleniumSettingsElementPrinter {

	void initialize(SeleniumTestTypeElement seleniumTestElement, NamedType outputName);
	
	void print(SeleniumSettingsContext settingsContext);
	
	void finish();

}