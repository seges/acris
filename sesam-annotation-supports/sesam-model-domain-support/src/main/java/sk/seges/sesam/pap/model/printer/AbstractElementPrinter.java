package sk.seges.sesam.pap.model.printer;

import java.io.PrintWriter;

import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.printer.api.ElementPrinter;

public abstract class AbstractElementPrinter implements ElementPrinter {
	
	protected final PrintWriter pw;
	
	public AbstractElementPrinter(PrintWriter pw) {
		this.pw = pw;
	}
	
	@Override
	public void initialize(ConfigurationTypeElement configurationTypeElement) {}
	
	@Override
	public void finish(ConfigurationTypeElement configurationTypeElement) {}
}