package sk.seges.sesam.pap.configuration.printer;

import javax.lang.model.element.TypeElement;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.pap.configuration.model.ProcessorContext;
import sk.seges.sesam.pap.configuration.printer.api.ElementPrinter;

public class GroupPrinter implements ElementPrinter {

	private ElementPrinter[] printers;
	
	public GroupPrinter(ElementPrinter... printers) {
		this.printers = printers;
	}
	
	@Override
	public void initialize(TypeElement type, NamedType outputName) {
		for (ElementPrinter printer: printers) {
			printer.initialize(type, outputName);
		}
	}

	@Override
	public void print(ProcessorContext context) {
		for (ElementPrinter printer: printers) {
			printer.print(context);
		}
	}

	@Override
	public void finish(TypeElement type) {
		for (ElementPrinter printer: printers) {
			printer.finish(type);
		}
	}
}
