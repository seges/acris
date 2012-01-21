package sk.seges.sesam.pap.service.printer;

import java.util.List;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterParameter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.service.model.ParametersFilter;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public class ServiceConstructorBodyPrinter extends AbstractPatameterCollectorPrinter {

	public ServiceConstructorBodyPrinter(TransferObjectProcessingEnvironment processingEnv, ParametersFilter parametersFilter, ParametersResolver parametersResolver, FormattedPrintWriter pw) {
		super(processingEnv, parametersFilter, parametersResolver, pw);
	}

	@Override
	public void print(ServiceConverterPrinterContext context) {
		super.print(context);
		pw.println();
		pw.println("this." + context.getLocalServiceFieldName() + " = " + context.getLocalServiceFieldName() + ";");
	}
	
	@Override
	public void finish(ServiceTypeElement serviceTypeElement) {
		
		List<ConverterParameter> params = mergeSameParams(converterParameters);

		for (ConverterParameter converterParameter : params) {
			pw.println("this." + converterParameter.getName() + " = " + converterParameter.getName() + ";");
		}

		pw.println("}");
		pw.println();
	}
}