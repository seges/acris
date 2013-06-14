package sk.seges.sesam.pap.service.printer.api;

import sk.seges.sesam.pap.converter.model.ConverterProviderType;
import sk.seges.sesam.pap.service.model.ConverterProviderContextType;

public interface ConverterContextElementPrinter {

	void initialize(ConverterProviderContextType contextType);
	
	void print(ConverterProviderType serviceConverterProvider);
	
	void finish(ConverterProviderContextType contextType);

}
