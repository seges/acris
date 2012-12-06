package sk.seges.corpis.pap.model.printer.converter;

import java.util.List;

import javax.lang.model.element.ExecutableElement;

import sk.seges.sesam.core.pap.model.ConverterParameter;
import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider.UsageType;

public class HibernateConverterProviderPrinter extends ConverterProviderPrinter {

	protected TransferObjectProcessingEnvironment processingEnv;
	
	public HibernateConverterProviderPrinter(FormattedPrintWriter pw, TransferObjectProcessingEnvironment processingEnv,
			ConverterConstructorParametersResolverProvider parametersResolverProvider, UsageType usageType) {
		super(pw, processingEnv, parametersResolverProvider, usageType);
		this.processingEnv = processingEnv;
	}
	
	private ParameterElement toParameter(ConverterParameter converterParameter) {
		return converterParameter.toParameterElement();
	}

	private ParameterElement[] toParameters(List<ConverterParameter> converterParameters) {
		ParameterElement[] result = new ParameterElement[converterParameters.size()];
		int i = 0;
		for (ConverterParameter converterParameter: converterParameters) {
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
