package sk.seges.sesam.pap.service.printer;

import sk.seges.sesam.pap.service.printer.api.NestedServiceConverterElementPrinter;
import sk.seges.sesam.pap.service.printer.model.NestedServiceConverterPrinterContext;

public class ConverterVerifier implements NestedServiceConverterElementPrinter {

	private boolean containsConverters = false;
	
	@Override
	public void initialize() {
	}

	@Override
	public void print(NestedServiceConverterPrinterContext context) {
		containsConverters = true;
	}

	public boolean isContainsConverter() {
		return containsConverters;
	}

	@Override
	public void finish() {
	}
}