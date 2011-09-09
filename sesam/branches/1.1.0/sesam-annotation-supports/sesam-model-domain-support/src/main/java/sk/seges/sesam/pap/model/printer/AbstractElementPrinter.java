package sk.seges.sesam.pap.model.printer;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.printer.api.ElementPrinter;

public abstract class AbstractElementPrinter implements ElementPrinter {
	
	protected final FormattedPrintWriter pw;
	
	public AbstractElementPrinter(FormattedPrintWriter pw) {
		this.pw = pw;
	}
	
	@Override
	public void initialize(ConfigurationTypeElement configurationTypeElement, NamedType outputName) {}
	
	@Override
	public void finish(ConfigurationTypeElement configurationTypeElement) {}
}