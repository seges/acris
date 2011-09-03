package sk.seges.sesam.pap.model.printer.field;

import java.io.PrintWriter;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.printer.AbstractElementPrinter;

public class FieldPrinter extends AbstractElementPrinter {

	public FieldPrinter(PrintWriter pw) {
		super(pw);
	}
	
	public void print(ProcessorContext context) {
		//we do not use modifier from the param - fields should be always private
		pw.println(Modifier.PRIVATE.toString() + " " + context.getFieldType().toString(ClassSerializer.CANONICAL, true) + " " + 
				context.getFieldName() + ";");
		pw.println();
	}
}