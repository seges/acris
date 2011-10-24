package sk.seges.pap.printer.table.column;

import javax.lang.model.element.ExecutableElement;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public abstract class TableColumnPrinter {
	protected final FormattedPrintWriter pw;
	protected final MutableDeclaredType vto;

	public TableColumnPrinter(FormattedPrintWriter pw, MutableDeclaredType vto) {
		super();
		this.pw = pw;
		this.vto = vto;
	}

	public abstract void print(ExecutableElement element);
}