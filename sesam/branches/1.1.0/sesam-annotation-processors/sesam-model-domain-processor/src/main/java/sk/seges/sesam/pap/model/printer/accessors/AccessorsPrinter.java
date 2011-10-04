package sk.seges.sesam.pap.model.printer.accessors;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.printer.AbstractElementPrinter;

public class AccessorsPrinter extends AbstractElementPrinter {

	public AccessorsPrinter(FormattedPrintWriter pw) {
		super(pw);
	}
	
	@Override
	public void print(TransferObjectContext context) {

		String modifier = Modifier.PUBLIC.toString() + " ";
		
		//modifier = context.getModifier() != null ? (context.getModifier().toString() + " ") : "";
		
		pw.println(modifier, context.getFieldType(), " " + MethodHelper.toGetter(context.getFieldName()) + " {");
		pw.println("return " + context.getFieldName() + ";");
		pw.println("}");
		pw.println();

		pw.println(modifier + "void " + MethodHelper.toSetter(context.getFieldName()) + 
				"(", context.getFieldType(), " " + context.getFieldName() + ") {");
		pw.println("this." + context.getFieldName() + " = " + context.getFieldName() + ";");
		pw.println("}");
		pw.println();
	}
}