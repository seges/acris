package sk.seges.corpis.pap.service.hibernate.printer;

import javax.lang.model.element.Modifier;

import sk.seges.corpis.pap.model.printer.converter.HibernateServiceConverterProviderParameterResolver;
import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.MutableExecutableType;
import sk.seges.sesam.core.pap.writer.HierarchyPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.pap.service.model.ConverterProviderContextType;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.converterprovider.ServiceConverterProviderContextPrinter;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public class ServiceConverterProviderContextMethodPrinter extends ServiceConverterProviderContextPrinter {

	public ServiceConverterProviderContextMethodPrinter(TransferObjectProcessingEnvironment processingEnv,
			ConverterConstructorParametersResolverProvider parametersResolverProvider,
			ConverterProviderPrinter converterProviderPrinter, ClassPathTypes classPathTypes) {
		super(processingEnv, parametersResolverProvider, converterProviderPrinter, classPathTypes);
	}
	
	@Override
	protected void initialize(ServiceConverterPrinterContext context) {

		UsageType previousUsageType = converterProviderPrinter.changeUsage(UsageType.CONVERTER_PROVIDER_CONTEXT_CONSTRUCTOR);

		ServiceTypeElement serviceTypeElement = context.getService();
		
		ConverterProviderContextType convertProviderContextType = new ConverterProviderContextType(serviceTypeElement, processingEnv);
		context.setConvertProviderContextType(convertProviderContextType);
		ParameterElement[] generatedParameters = convertProviderContextType.getRequiredParameters(
				parametersResolverProvider.getParameterResolver(UsageType.CONVERTER_PROVIDER_CONTEXT_CONSTRUCTOR),
				parametersResolverProvider.getParameterResolver(UsageType.DEFINITION));

		serviceTypeElement.getServiceConverter().addNestedType(convertProviderContextType);
		
		ParameterElement[] requiredParameters = 
				convertProviderContextType.getConverterParameters(parametersResolverProvider.getParameterResolver(UsageType.CONVERTER_PROVIDER_CONTEXT_CONSTRUCTOR));

		MutableExecutableType converterProviderContextMethod = processingEnv.getTypeUtils().getExecutable(convertProviderContextType, HibernateServiceConverterProviderParameterResolver.GET_CONVERTER_PROVIDER_CONTEXT_METHOD);
		converterProviderContextMethod.addModifier(Modifier.PROTECTED);
		serviceTypeElement.getServiceConverter().addMethod(converterProviderContextMethod);

		HierarchyPrintWriter pw = converterProviderContextMethod.getPrintWriter();
		
		for (ParameterElement generatedParameter: generatedParameters) {
			converterProviderContextMethod.addParameter(processingEnv.getElementUtils().getParameterElement(generatedParameter.getType(), generatedParameter.getName()));
		}

		pw.print("return new " + convertProviderContextType.getSimpleName() + "(");
		int i = 0;
		for (ParameterElement parameter: requiredParameters) {
			if (i > 0) {
				pw.print(", ");
			}

			pw.print(parameter.getName());
			i++;
		}
		pw.println(");");
		
		converterProviderPrinter.changeUsage(previousUsageType);
		
		super.initialize(context);
	}
}