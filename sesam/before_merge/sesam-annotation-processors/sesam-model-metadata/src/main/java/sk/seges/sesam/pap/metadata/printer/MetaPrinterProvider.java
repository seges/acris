package sk.seges.sesam.pap.metadata.printer;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class MetaPrinterProvider {

	private final FormattedPrintWriter pw;
	
	public MetaPrinterProvider(FormattedPrintWriter pw) {
		this.pw = pw;
	}
	
	public NestedPrinter getNestedPrinter() {
		return new NestedPrinter(pw, this);
	}
	
	public ConstantsPrinter getConstantsPrinter() {
		return new ConstantsPrinter(pw);
	}
}