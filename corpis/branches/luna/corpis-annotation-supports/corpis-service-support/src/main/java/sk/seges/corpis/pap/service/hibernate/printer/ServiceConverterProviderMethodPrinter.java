package sk.seges.corpis.pap.service.hibernate.printer;

import javax.lang.model.element.Modifier;

import sk.seges.corpis.pap.model.printer.converter.HibernateServiceConverterProviderPrinter;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.service.model.ServiceConvertProviderType;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.ServiceConverterProviderPrinter;

public class ServiceConverterProviderMethodPrinter extends ServiceConverterProviderPrinter {

	public ServiceConverterProviderMethodPrinter(TransferObjectProcessingEnvironment processingEnv,
			ConverterConstructorParametersResolver parametersResolver, FormattedPrintWriter pw,
			ConverterProviderPrinter converterProviderPrinter) {
		super(processingEnv, parametersResolver, pw, converterProviderPrinter);
	}

	@Override
	protected void initialize(ServiceTypeElement serviceTypeElement) {

		ServiceConvertProviderType serviceConvertProviderType = new ServiceConvertProviderType(serviceTypeElement, processingEnv);
		ParameterElement[] generatedParameters = serviceConvertProviderType.getConverterParameters(parametersResolver);
				
		pw.print(Modifier.PROTECTED.name().toLowerCase() + " ", serviceConvertProviderType, " " + HibernateServiceConverterProviderPrinter.GET_CONVERTER_PROVIDER_METHOD + "(");

		int i = 0;
		for (ParameterElement generatedParameter: generatedParameters) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print(generatedParameter.getType(), " " + generatedParameter.getName());
			i++;
		}

		pw.println(") {");
		pw.print("return new ", serviceConvertProviderType, "(");
		i = 0;
		for (ParameterElement generatedParameter: generatedParameters) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print(generatedParameter.getName());
			i++;
		}
		pw.println(");");
		pw.println("}");
		pw.println();
		super.initialize(serviceTypeElement);
	}	
}