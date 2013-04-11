package sk.seges.sesam.pap.converter.printer;

import sk.seges.sesam.pap.converter.printer.api.ConverterProviderElementPrinter;
import sk.seges.sesam.pap.converter.printer.model.ConverterProviderPrinterContext;

public class ConverterVerifier implements ConverterProviderElementPrinter {

	private boolean containsConverters = false;
	
	@Override
	public void initialize() {
	}

	@Override
	public void print(ConverterProviderPrinterContext context) {
		containsConverters = true;
	}

	public boolean isContainsConverter() {
		return containsConverters;
	}

	@Override
	public void finish() {
	}
}