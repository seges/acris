package sk.seges.corpis.core.pap.dao.printer;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;

public class CrudPrinter implements DaoPrinter {

	private FormattedPrintWriter pw;
	
	public CrudPrinter(FormattedPrintWriter pw) {
		this.pw = pw;
	}

	@Override
	public void print(DaoContext context) {
		MutableDeclaredType declaredType = context.getProcessingEnv().getTypeUtils().toMutableType(Class.class);
		declaredType.setTypeVariables(context.getProcessingEnv().getTypeUtils().getWildcardType(context.getDomainType(), null));
		
		pw.println("public ", context.getDaoType(), "() {");
		pw.println("super(", context.getDomainType(), ".class);");
		pw.println("}");
		pw.println();
		pw.println("public ", context.getDaoType(), "(", declaredType , " clazz) {");
		pw.println("super(clazz);");
		pw.println("}");
	}
}