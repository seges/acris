package sk.seges.sesam.pap.metadata.printer;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.metadata.model.MetaModelContext;

public class ConstantsPrinter {

	private final FormattedPrintWriter pw;
	
	public ConstantsPrinter(FormattedPrintWriter pw) {
		this.pw = pw;
	}

	public void print(MetaModelContext context) {
		pw.println("public static final ", String.class, " " + context.getFieldName() + " = \"" + context.getPath() + "\";");
		pw.println();
	}
}
