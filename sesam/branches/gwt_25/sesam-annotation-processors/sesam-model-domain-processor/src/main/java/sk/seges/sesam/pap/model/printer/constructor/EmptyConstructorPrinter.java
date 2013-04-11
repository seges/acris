package sk.seges.sesam.pap.model.printer.constructor;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.printer.AbstractElementPrinter;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;

public class EmptyConstructorPrinter extends AbstractElementPrinter implements TransferObjectElementPrinter {

	private boolean initialized = false;
	
	public EmptyConstructorPrinter(FormattedPrintWriter pw) {
		super(pw);
	}

	@Override
	public void initialize(ConfigurationTypeElement configurationTypeElement, MutableDeclaredType outputName) {}

	@Override
	public void finish(ConfigurationTypeElement configurationTypeElement) {}

	@Override
	public void print(TransferObjectContext context) {
		if (!initialized) {
			pw.println("public " + context.getConfigurationTypeElement().getDto().getSimpleName() + "() {}");
			pw.println();
			initialized = true;
		}
	}
}