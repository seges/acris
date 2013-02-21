package sk.seges.sesam.pap.service.printer;

import java.util.List;

import sk.seges.sesam.core.pap.model.ConverterConstructorParameter;
import sk.seges.sesam.core.pap.writer.HierarchyPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.service.model.ServiceConverterParametersFilter;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public class ServiceConstructorBodyPrinter extends AbstractParameterCollectorPrinter {

	public ServiceConstructorBodyPrinter(TransferObjectProcessingEnvironment processingEnv, ServiceConverterParametersFilter parametersFilter, 
			ConverterConstructorParametersResolverProvider parametersResolverProvider) {
		super(processingEnv, parametersFilter, parametersResolverProvider);
	}

	@Override
	public void print(ServiceConverterPrinterContext context) {
		super.print(context);
		
		HierarchyPrintWriter pw = context.getService().getServiceConverter().getConstructor().getPrintWriter();
		
		pw.println();
		pw.println("this." + context.getLocalServiceFieldName() + " = " + context.getLocalServiceFieldName() + ";");
	}
	
	@Override
	public void finish(ServiceTypeElement serviceTypeElement) {
		
		List<ConverterConstructorParameter> params = mergeSameParams(converterParameters);

		HierarchyPrintWriter pw = serviceTypeElement.getServiceConverter().getConstructor().getPrintWriter();

		for (ConverterConstructorParameter converterParameter : params) {
			pw.println("this." + converterParameter.getName() + " = " + converterParameter.getName() + ";");
		}
	}
}