package sk.seges.corpis.core.pap.dao.printer;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class EntityInstancerPrinter implements DaoPrinter {

	private FormattedPrintWriter pw;
	
	public EntityInstancerPrinter(FormattedPrintWriter pw) {
		this.pw = pw;
	}
	
	@Override
	public void print(DaoContext context) {
		pw.println("@Override");
		pw.println("public ", context.getDomainType(), " getEntityInstance() {");
		pw.println("return new ", context.getDomainType(), "();");
		pw.println("}");
	}
}