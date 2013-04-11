package sk.seges.corpis.pap.service.hibernate.printer;

import java.util.List;

import javax.lang.model.element.Modifier;

import sk.seges.corpis.pap.model.printer.converter.HibernateServiceConverterProviderParameterResolver;
import sk.seges.sesam.core.pap.builder.api.ClassPathTypes;
import sk.seges.sesam.core.pap.model.api.ClassSerializer;
import sk.seges.sesam.core.pap.model.mutable.api.MutableExecutableType;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableVariableElement;
import sk.seges.sesam.core.pap.writer.HierarchyPrintWriter;
import sk.seges.sesam.core.pap.writer.LazyPrintWriter;
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
	protected void initialize(final ServiceConverterPrinterContext context) {

		UsageType previousUsageType = converterProviderPrinter.changeUsage(UsageType.CONVERTER_PROVIDER_CONTEXT_CONSTRUCTOR);

		ServiceTypeElement serviceTypeElement = context.getService();
		
		final ConverterProviderContextType convertProviderContextType = new ConverterProviderContextType(serviceTypeElement, processingEnv);
		context.setConvertProviderContextType(convertProviderContextType);
//		ParameterElement[] generatedParameters = convertProviderContextType.getRequiredParameters(
//				parametersResolverProvider.getParameterResolver(UsageType.CONVERTER_PROVIDER_CONTEXT_CONSTRUCTOR),
//				parametersResolverProvider.getParameterResolver(UsageType.DEFINITION));

		serviceTypeElement.getServiceConverter().addNestedType(convertProviderContextType);
				
//		ParameterElement[] requiredParameters = 
//				convertProviderContextType.getConverterParameters(parametersResolverProvider.getParameterResolver(UsageType.CONVERTER_PROVIDER_CONTEXT_CONSTRUCTOR));

		final MutableExecutableType converterProviderContextMethod = processingEnv.getTypeUtils().getExecutable(convertProviderContextType, HibernateServiceConverterProviderParameterResolver.GET_CONVERTER_PROVIDER_CONTEXT_METHOD);
		converterProviderContextMethod.addModifier(Modifier.PROTECTED);
		serviceTypeElement.getServiceConverter().addMethod(converterProviderContextMethod);

		HierarchyPrintWriter methodPrinter = converterProviderContextMethod.getPrintWriter();

		methodPrinter.addLazyPrinter(new LazyPrintWriter(processingEnv) {
			
			@Override
			protected void print() {
				// TODO Auto-generated method stub
				
				List<MutableVariableElement> requiredParameters = context.getConvertProviderContextType().getConstructor().getParameters();

				List<MutableVariableElement> localFields = context.getService().getServiceConverter().getFields();

				for (MutableVariableElement generatedParameter: requiredParameters) {
					if (getLocalField(localFields, generatedParameter) == null) {
						converterProviderContextMethod.addParameter(processingEnv.getElementUtils().getParameterElement(generatedParameter.asType(), generatedParameter.getSimpleName()));
					}
				}

				print("return new " + convertProviderContextType.getSimpleName() + "(");
				int i = 0;
				for (MutableVariableElement parameter: requiredParameters) {
					if (i > 0) {
						print(", ");
					}

					MutableVariableElement localField = getLocalField(localFields, parameter);
					
					if (localField == null) {
						print(parameter.getSimpleName());
					} else {
						print(localField.getSimpleName());
					}
					i++;
				}
				println(");");
			}
		});
		
		converterProviderPrinter.changeUsage(previousUsageType);
		
		super.initialize(context);
	}
	
	private MutableVariableElement getLocalField(List<MutableVariableElement> localFields, MutableVariableElement parameter) {
		for (MutableVariableElement localField: localFields) {
			if (localField.getSimpleName().equals(parameter.getSimpleName()) && 
					localField.asType().toString(ClassSerializer.CANONICAL, false).equals(parameter.asType().toString(ClassSerializer.CANONICAL, false))) {
				return localField;
			}
		}
		
		return null;
	}
}