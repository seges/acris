package sk.seges.pap.printer.table.column;

import javax.lang.model.element.ExecutableElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

import com.google.gwt.user.cellview.client.TextColumn;

public class TextColumnPrinter extends TableColumnPrinter {

	public TextColumnPrinter(FormattedPrintWriter pw, MutableDeclaredType vto) {
		super(pw, vto);
	}

	@Override
	public void print(ExecutableElement element) {
		String columnVar = element.getSimpleName() + "Column";
		pw.println(TextColumn.class, "<", vto, "> ", columnVar, " = new ",
				TextColumn.class, "<", vto, ">() {");
		pw.println("@Override");
		pw.println("public String getValue(", vto, " value) {");
		pw.println("return value.", MethodHelper.toGetter(element),
				".toString();");
		pw.println("}");

		pw.println("};");

		pw.println("table.addColumn(", columnVar, ", ", "\"",
				element.getSimpleName(), "\");");
	}
}