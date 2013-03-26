package sk.seges.corpis.pap.model.printer.converter;

import java.util.List;

import javax.lang.model.element.ExecutableElement;

import sk.seges.sesam.core.pap.model.ConverterConstructorParameter;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;

public class HibernateConverterProviderPrinter extends ConverterProviderPrinter {

	protected TransferObjectProcessingEnvironment processingEnv;
	
	public HibernateConverterProviderPrinter(TransferObjectProcessingEnvironment processingEnv,
			ConverterConstructorParametersResolverProvider parametersResolverProvider, UsageType usageType) {
		super(processingEnv, parametersResolverProvider, usageType);
		this.processingEnv = processingEnv;
	}
	
	private ParameterElement toParameter(ConverterConstructorParameter converterParameter) {
		return converterParameter.toParameterElement();
	}

	private ParameterElement[] toParameters(List<ConverterConstructorParameter> converterParameters) {
		ParameterElement[] result = new ParameterElement[converterParameters.size()];
		int i = 0;
		for (ConverterConstructorParameter converterParameter: converterParameters) {
			result[i++] = toParameter(converterParameter);
		}
		return result;
	}
	
	@Override
	protected ParameterElement[] getConverterParameters(ConverterTypeElement converterTypeElement, ExecutableElement method) {
		
		if (converterTypeElement == null) {
			return parametersResolverProvider.getParameterResolver(usageType).getConstructorAditionalParameters();
		}
		
		return toParameters(converterTypeElement.getConverterParameters(parametersResolverProvider.getParameterResolver(usageType)));
	}
}
