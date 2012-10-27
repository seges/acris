package sk.seges.sesam.pap.metadata.printer;

import java.util.HashSet;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.metadata.model.MetaModelContext;

public class NestedPrinter {

	private final FormattedPrintWriter pw;
	private HashSet<String> localCache;
	private MetaPrinterProvider printerProvider;
	
	public NestedPrinter(FormattedPrintWriter pw, MetaPrinterProvider printerProvider) {
		this.pw = pw;
		this.localCache= new HashSet<String>();
		this.printerProvider = printerProvider;
	}
	
	public void initialize(MetaModelContext context) {

		if (!context.isNested()) {
			return;
		}
		
		String convertedName = context.getConverter().getConvertedPropertyName(context.getProperty());

		pw.println("public static interface " +  convertedName + " {");
		pw.println();

		MetaModelContext thisContext = context.clone().setProperty(MetaModelContext.THIS);
		
		if (localCache.add(thisContext.getFieldName())) {
			printerProvider.getConstantsPrinter().print(thisContext);
		}
	}
	
	public void finish(MetaModelContext context) {
		if (!context.isNested()) {
			return;
		}

		pw.println("}");
		pw.println();
	}
}