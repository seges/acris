package sk.seges.sesam.pap.service.printer.converterprovider;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.ElementFilter;

import sk.seges.sesam.core.pap.model.ConstructorParameter;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.ParameterElement.ParameterUsageContext;
import sk.seges.sesam.core.pap.model.ParameterElement.ParameterUsageProvider;
import sk.seges.sesam.core.pap.model.mutable.api.MutableReferenceType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableProcessingEnvironment;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.HierarchyPrintWriter;
import sk.seges.sesam.pap.converter.printer.converterprovider.ConverterProviderPrinterDelegate;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.model.utils.ConstructorHelper;
import sk.seges.sesam.pap.service.model.ConverterProviderContextType;
import sk.seges.sesam.pap.service.model.ServiceConverterTypeElement;

public class ConverterProviderContextPrinterDelegate {

	private ConverterProviderPrinterDelegate printerDelegate;
	
	private final ConverterConstructorParametersResolverProvider parametersResolverProvider;
	
	public ConverterProviderContextPrinterDelegate(ConverterConstructorParametersResolverProvider parametersResolverProvider) {
		this.parametersResolverProvider = parametersResolverProvider;
	}
	
	public void finalize() {}
	
	public void initialize(final MutableProcessingEnvironment processingEnv, final ConverterProviderContextType converterProviderType, 
			final ServiceConverterTypeElement serviceConverterTypeElement, UsageType usageType, final Set<? extends Element> converterPrinterDelegates) {

		printerDelegate = new ConverterProviderPrinterDelegate(parametersResolverProvider) {
			@Override
			public void printConstructorBody(HierarchyPrintWriter pw) {
				
				for (Element converterPrinterDelegate: converterPrinterDelegates) {
					pw.print("registerConverterProvider(new ", converterPrinterDelegate, "(");
					ConverterConstructorParametersResolver parameterResolver = parametersResolverProvider.getParameterResolver(UsageType.CONVERTER_PROVIDER_CONSTRUCTOR);
					ParameterElement[] converterParameters = converterProviderType.getConverterParameters(parameterResolver);

					List<ExecutableElement> constructors = ElementFilter.constructorsIn(converterPrinterDelegate.getEnclosedElements());
					
					List<ConstructorParameter> constructorParameters = new ArrayList<ConstructorParameter>();
					
					if (constructors.size() > 0) {
						constructorParameters = ConstructorHelper.getConstructorParameters(processingEnv.getTypeUtils(), constructors.get(0));
					}
					
					int j = 0;
//					for (ParameterElement converterParameter: converterParameters) {
					for (final ConstructorParameter constructorParameter: constructorParameters) {
						if (j > 0) {
							pw.print(", ");
						}
						ParameterElement parameterElement = getParameterElementByType(constructorParameter, converterParameters);
						if (parameterElement == null) {
							parameterElement = new ParameterElement(constructorParameter.getType(), constructorParameter.getName(), new ParameterUsageProvider() {
								
								@Override
								public MutableType getUsage(ParameterUsageContext context) {
									return constructorParameter.getType();
								}
							}, true);
							
							converterProviderType.getConstructor().addParameter(processingEnv.getElementUtils().getParameterElement(
									constructorParameter.getType(), constructorParameter.getName()));
							
							ProcessorUtils.addField(processingEnv, serviceConverterTypeElement, constructorParameter.getType(), constructorParameter.getName());
							//throw new RuntimeException("There is no parameter of type " + constructorParameter.getType() + " available for constructor of element " + converterPrinterDelegate);
						}
						
						MutableType usage = parameterElement.getUsage(new ParameterUsageContext() {
							
							@Override
							public ExecutableElement getMethod() {
								return null;
							}
						});
						
						if (usage instanceof MutableReferenceType) {
							pw.print(usage);
						} else {
							pw.print(parameterElement.getName());
						}

						j++;
					}
					pw.println("));");
				}
			}
		};

		printerDelegate.initialize(processingEnv, converterProviderType, usageType);
	}
}