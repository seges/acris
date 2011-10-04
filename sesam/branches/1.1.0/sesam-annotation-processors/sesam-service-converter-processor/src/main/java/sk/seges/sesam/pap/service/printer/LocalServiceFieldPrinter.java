package sk.seges.sesam.pap.service.printer;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.api.ServiceConverterElementPrinter;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public class LocalServiceFieldPrinter implements ServiceConverterElementPrinter {

	private final FormattedPrintWriter pw;
	
	public LocalServiceFieldPrinter(FormattedPrintWriter pw) {
		this.pw = pw;
	}
	
	@Override
	public void initialize(ServiceTypeElement serviceTypeElement, MutableDeclaredType outputName) {}

	@Override
	public void print(ServiceConverterPrinterContext context) {
		pw.println("private ", context.getLocalServiceInterface(), " " + context.getLocalServiceFieldName() + ";");
	}

	@Override
	public void finish(ServiceTypeElement serviceTypeElement) {}

}
