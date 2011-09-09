package sk.seges.sesam.pap.model.printer.constructor;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.printer.AbstractElementPrinter;
import sk.seges.sesam.pap.model.printer.api.ElementPrinter;

public class EnumeratedConstructorBodyPrinter extends AbstractElementPrinter implements ElementPrinter {

	public EnumeratedConstructorBodyPrinter(FormattedPrintWriter pw) {
		super(pw);
	}

	@Override
	public void print(ProcessorContext context) {
		pw.println("this." + context.getFieldName() + " = " + context.getFieldName() + ";");
	}

	@Override
	public void finish(ConfigurationTypeElement configuratioTypeElement) {
		pw.println("}");
		pw.println();
	}
}