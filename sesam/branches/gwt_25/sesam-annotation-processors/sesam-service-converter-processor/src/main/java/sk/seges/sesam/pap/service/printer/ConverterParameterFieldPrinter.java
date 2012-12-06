package sk.seges.sesam.pap.service.printer;

import sk.seges.sesam.core.pap.model.ConverterParameter;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.service.model.ServiceConverterParametersFilter;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

public class ConverterParameterFieldPrinter extends AbstractParameterCollectorPrinter {
		
	public ConverterParameterFieldPrinter(TransferObjectProcessingEnvironment processingEnv, ServiceConverterParametersFilter parametersFilter, 
			ConverterConstructorParametersResolverProvider parametersResolverProvider, FormattedPrintWriter pw) {
		super(processingEnv, parametersFilter, parametersResolverProvider, pw);
	}

	@Override
	public void finish(ServiceTypeElement serviceTypeElement) {
		for (ConverterParameter converterParameter: converterParameters) {
			pw.println("protected ", converterParameter.getType(), " " + converterParameter.getName() + ";");
			pw.println();
		}
	}	
}