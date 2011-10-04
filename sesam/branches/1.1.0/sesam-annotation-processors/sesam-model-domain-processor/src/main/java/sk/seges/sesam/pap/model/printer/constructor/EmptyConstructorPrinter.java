package sk.seges.sesam.pap.model.printer.constructor;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.printer.AbstractElementPrinter;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;

public class EmptyConstructorPrinter extends AbstractElementPrinter implements TransferObjectElementPrinter {

	public EmptyConstructorPrinter(FormattedPrintWriter pw) {
		super(pw);
	}

	@Override
	public void initialize(ConfigurationTypeElement configurationTypeElement, MutableDeclaredType outputName) {
		pw.println("public " + outputName.getSimpleName() + "() {");
	}

	@Override
	public void finish(ConfigurationTypeElement configurationTypeElement) {
		pw.println("}");
	}

	@Override
	public void print(TransferObjectContext context) {
	}
}
