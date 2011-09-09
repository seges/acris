package sk.seges.sesam.pap.model.printer.constructor;

import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.printer.AbstractElementPrinter;
import sk.seges.sesam.pap.model.printer.api.ElementPrinter;

public class EmptyConstructorPrinter extends AbstractElementPrinter implements ElementPrinter {

	public EmptyConstructorPrinter(FormattedPrintWriter pw) {
		super(pw);
	}

	@Override
	public void initialize(ConfigurationTypeElement configurationTypeElement, NamedType outputName) {
		pw.println("public " + outputName.getSimpleName() + "() {");
	}

	@Override
	public void finish(ConfigurationTypeElement configurationTypeElement) {
		pw.println("}");
	}

	@Override
	public void print(ProcessorContext context) {
	}
}
