package sk.seges.sesam.pap.converter.printer.api;

import sk.seges.sesam.pap.converter.printer.model.ConverterProviderPrinterContext;

public interface ConverterProviderElementPrinter {

	void initialize();
	
	void print(ConverterProviderPrinterContext context);
	
	void finish();

}
