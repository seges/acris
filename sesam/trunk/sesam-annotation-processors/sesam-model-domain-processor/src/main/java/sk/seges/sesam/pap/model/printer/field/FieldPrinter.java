package sk.seges.sesam.pap.model.printer.field;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.printer.AbstractElementPrinter;

public class FieldPrinter extends AbstractElementPrinter {

	public FieldPrinter(FormattedPrintWriter pw) {
		super(pw);
	}
	
	public void print(TransferObjectContext context) {
		//we do not use modifier from the param - fields should be always private
		pw.println(Modifier.PRIVATE.toString() + " ", context.getDtoFieldType(), " " + context.getDtoFieldName() + ";");
		pw.println();
	}
}