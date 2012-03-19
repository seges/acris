package sk.seges.sesam.pap.service.printer.api;

import sk.seges.sesam.pap.service.printer.model.NestedServiceConverterPrinterContext;

public interface NestedServiceConverterElementPrinter {

	void initialize();
	
	void print(NestedServiceConverterPrinterContext context);
	
	void finish();

}
