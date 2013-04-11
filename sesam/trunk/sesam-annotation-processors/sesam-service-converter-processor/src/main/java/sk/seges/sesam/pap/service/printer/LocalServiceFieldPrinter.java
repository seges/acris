package sk.seges.sesam.pap.service.printer;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableVariableElement;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.api.ServiceConverterElementPrinter;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public class LocalServiceFieldPrinter implements ServiceConverterElementPrinter {

	@Override
	public void initialize(ServiceTypeElement serviceTypeElement, MutableDeclaredType outputName) {}

	@Override
	public void print(ServiceConverterPrinterContext context) {
		context.getService().getServiceConverter().addField((MutableVariableElement) context.getProcessingEnv().getElementUtils().getParameterElement(
				context.getLocalServiceInterface(), context.getLocalServiceFieldName()).addModifier(Modifier.PROTECTED));
	}

	@Override
	public void finish(ServiceTypeElement serviceTypeElement) {}

}
