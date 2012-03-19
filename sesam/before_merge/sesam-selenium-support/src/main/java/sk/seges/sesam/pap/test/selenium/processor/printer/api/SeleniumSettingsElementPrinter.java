package sk.seges.sesam.pap.test.selenium.processor.printer.api;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumSettingsContext;
import sk.seges.sesam.pap.test.selenium.processor.model.SeleniumTestCaseType;

public interface SeleniumSettingsElementPrinter {

	void initialize(SeleniumTestCaseType seleniumTestElement, MutableDeclaredType outputName);
	
	void print(SeleniumSettingsContext settingsContext);
	
	void finish();

}