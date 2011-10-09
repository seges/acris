package sk.seges.sesam.pap.configuration.printer;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.pap.configuration.model.SettingsContext;
import sk.seges.sesam.pap.configuration.printer.api.SettingsElementPrinter;

public class GroupPrinter implements SettingsElementPrinter {

	private SettingsElementPrinter[] printers;
	
	public GroupPrinter(SettingsElementPrinter... printers) {
		this.printers = printers;
	}
	
	@Override
	public void initialize(TypeElement type, MutableDeclaredType outputName) {
		for (SettingsElementPrinter printer: printers) {
			printer.initialize(type, outputName);
		}
	}

	@Override
	public void print(SettingsContext context) {
		for (SettingsElementPrinter printer: printers) {
			printer.print(context);
		}
	}

	@Override
	public void finish(TypeElement type) {
		for (SettingsElementPrinter printer: printers) {
			printer.finish(type);
		}
	}
}
