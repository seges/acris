package sk.seges.sesam.pap.service.printer;

import java.util.List;

import sk.seges.sesam.core.pap.model.ConverterConstructorParameter;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.service.model.ServiceConverterParametersFilter;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public class ServiceConstructorDefinitionPrinter extends AbstractParameterCollectorPrinter {

	private int index = 0;

	public ServiceConstructorDefinitionPrinter(TransferObjectProcessingEnvironment processingEnv, ServiceConverterParametersFilter parametersFilter,
			ConverterConstructorParametersResolverProvider parametersResolverProvider, FormattedPrintWriter pw) {
		super(processingEnv, parametersFilter, parametersResolverProvider, pw);
	}

	@Override
	public void initialize(ServiceTypeElement serviceTypeElement, MutableDeclaredType outputName) {
		super.initialize(serviceTypeElement, outputName);
		pw.println();
		pw.print("public " + outputName.getSimpleName() + "(");
	}

	@Override
	public void print(ServiceConverterPrinterContext context) {
		super.print(context);

		if (index > 0) {
			pw.print(", ");
		}

		pw.print(context.getLocalServiceInterface(), " " + context.getLocalServiceFieldName());
		index++;
	}
	
	@Override
	public void finish(ServiceTypeElement serviceTypeElement) {
		List<ConverterConstructorParameter> params = mergeSameParams(converterParameters);

		for (ConverterConstructorParameter converterParameter: params) {
			pw.print(", ", converterParameter.getType(), " " + converterParameter.getName());
		}
		
		pw.println(") {");

	}
}