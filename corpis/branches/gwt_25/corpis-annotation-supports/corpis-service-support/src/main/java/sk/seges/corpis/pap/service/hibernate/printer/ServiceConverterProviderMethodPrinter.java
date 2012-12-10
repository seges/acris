package sk.seges.corpis.pap.service.hibernate.printer;

import javax.lang.model.element.Modifier;

import sk.seges.corpis.pap.model.printer.converter.HibernateServiceConverterProviderParameterResolver;
import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.pap.service.model.ServiceConvertProviderType;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.converterprovider.ServiceConverterProviderPrinter;

public class ServiceConverterProviderMethodPrinter extends ServiceConverterProviderPrinter {

	public ServiceConverterProviderMethodPrinter(TransferObjectProcessingEnvironment processingEnv,
			ConverterConstructorParametersResolverProvider parametersResolverProvider, FormattedPrintWriter pw,
			ConverterProviderPrinter converterProviderPrinter, ClassPathTypes classPathTypes) {
		super(processingEnv, parametersResolverProvider, pw, converterProviderPrinter, classPathTypes);
	}

	@Override
	protected void initialize(ServiceTypeElement serviceTypeElement) {

		UsageType previousUsageType = converterProviderPrinter.changeUsage(UsageType.USAGE_CONSTRUCTOR_CONVERTER_PROVIDER);

		ServiceConvertProviderType serviceConvertProviderType = new ServiceConvertProviderType(serviceTypeElement, processingEnv);
		ParameterElement[] generatedParameters = serviceConvertProviderType.getConverterParameters(parametersResolverProvider.getParameterResolver(UsageType.USAGE_CONSTRUCTOR_CONVERTER_PROVIDER));
				
		pw.print(Modifier.PROTECTED.name().toLowerCase() + " ", serviceConvertProviderType, " " + HibernateServiceConverterProviderParameterResolver.GET_CONVERTER_PROVIDER_METHOD + "(");

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
		
		converterProviderPrinter.changeUsage(previousUsageType);
		
		super.initialize(serviceTypeElement);
	}
}