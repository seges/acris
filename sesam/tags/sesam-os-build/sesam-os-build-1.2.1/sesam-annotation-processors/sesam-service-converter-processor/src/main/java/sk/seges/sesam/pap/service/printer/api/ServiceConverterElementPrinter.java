package sk.seges.sesam.pap.service.printer.api;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public interface ServiceConverterElementPrinter {

	void initialize(ServiceTypeElement serviceTypeElement, MutableDeclaredType outputName);
	
	void print(ServiceConverterPrinterContext context);
	
	void finish(ServiceTypeElement serviceTypeElement);

}
