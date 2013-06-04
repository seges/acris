package sk.seges.sesam.pap.model.printer.field;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.model.metadata.strategy.PojoPropertyConverter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.printer.AbstractElementPrinter;

public class FieldPrinter extends AbstractElementPrinter {

	public FieldPrinter(FormattedPrintWriter pw) {
		super(pw);
	}
	
	public void print(TransferObjectContext context) {
		String convertedPropertyName = new PojoPropertyConverter().getConvertedPropertyName(context.getDtoFieldName());
		//we do not use modifier from the param - fields should be always private
		pw.println(Modifier.PUBLIC.toString() + " " + Modifier.STATIC.toString() + " " + Modifier.FINAL.toString() + " ", 
				String.class, " " + convertedPropertyName + " = \"" + context.getDtoFieldName() + "\";");
		pw.println();
		pw.println(Modifier.PRIVATE.toString() + " ", context.getDtoFieldType(), " " + context.getDtoFieldName() + ";");
		pw.println();
	}
}