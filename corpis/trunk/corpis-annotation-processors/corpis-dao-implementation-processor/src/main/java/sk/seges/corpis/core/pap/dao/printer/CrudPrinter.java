package sk.seges.corpis.core.pap.dao.printer;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class CrudPrinter implements DaoPrinter {

	private FormattedPrintWriter pw;
	
	public CrudPrinter(FormattedPrintWriter pw) {
		this.pw = pw;
	}

	@Override
	public void print(DaoContext context) {
		pw.println("public " + context.getDaoType().getSimpleName() + "() {");
		pw.println("super(" + context.getDomainType().getSimpleName() + ".class);");
		pw.println("}");
	}
}