package sk.seges.sesam.pap.model.printer;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;

public abstract class AbstractElementPrinter implements TransferObjectElementPrinter {
	
	protected final FormattedPrintWriter pw;
	
	public AbstractElementPrinter(FormattedPrintWriter pw) {
		this.pw = pw;
	}
	
	@Override
	public void initialize(ConfigurationTypeElement configurationTypeElement, MutableDeclaredType outputName) {}
	
	@Override
	public void finish(ConfigurationTypeElement configurationTypeElement) {}
}