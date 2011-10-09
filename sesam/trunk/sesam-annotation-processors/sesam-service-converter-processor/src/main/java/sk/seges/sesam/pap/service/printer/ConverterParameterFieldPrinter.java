package sk.seges.sesam.pap.service.printer;

import java.util.List;

import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterParameter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

public class ConverterParameterFieldPrinter extends AbstractPatameterCollectorPrinter {
		
	public ConverterParameterFieldPrinter(TransferObjectProcessingEnvironment processingEnv, ParametersResolver parametersResolver, FormattedPrintWriter pw) {
		super(processingEnv, parametersResolver, pw);
	}

	@Override
	public void finish(ServiceTypeElement serviceTypeElement) {
		List<ConverterParameter> params = mergeSameParams(converterParameters);

		for (ConverterParameter converterParameter: params) {
			pw.println("private ", converterParameter.getType(), " " + converterParameter.getName() + ";");
			pw.println();
		}
	}	
}