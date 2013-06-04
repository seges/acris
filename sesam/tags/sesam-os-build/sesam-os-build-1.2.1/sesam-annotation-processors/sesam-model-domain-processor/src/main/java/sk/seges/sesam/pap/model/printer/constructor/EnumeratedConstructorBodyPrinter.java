package sk.seges.sesam.pap.model.printer.constructor;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.printer.AbstractElementPrinter;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;

public class EnumeratedConstructorBodyPrinter extends AbstractElementPrinter implements TransferObjectElementPrinter {

	public EnumeratedConstructorBodyPrinter(FormattedPrintWriter pw) {
		super(pw);
	}

	@Override
	public void print(TransferObjectContext context) {
		pw.println("this." + context.getDtoFieldName() + " = " + context.getDtoFieldName() + ";");
	}

	@Override
	public void finish(ConfigurationTypeElement configuratioTypeElement) {
		pw.println("}");
		pw.println();
	}
}