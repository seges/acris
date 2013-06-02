package sk.seges.sesam.pap.service.printer;

import java.util.List;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.ConverterConstructorParameter;
import sk.seges.sesam.core.pap.model.mutable.api.MutableExecutableType;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.service.model.ServiceConverterParametersFilter;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public class ServiceConstructorDefinitionPrinter extends AbstractParameterCollectorPrinter {

	public ServiceConstructorDefinitionPrinter(TransferObjectProcessingEnvironment processingEnv, ServiceConverterParametersFilter parametersFilter,
			ConverterConstructorParametersResolverProvider parametersResolverProvider) {
		super(processingEnv, parametersFilter, parametersResolverProvider);
	}

	@Override
	public void print(ServiceConverterPrinterContext context) {
		super.print(context);
		context.getService().getServiceConverter().getConstructor().
			addParameter(processingEnv.getElementUtils().getParameterElement(ProcessorUtils.replaceTypeVariablesByWildcards(context.getLocalServiceInterface().clone()), 
					context.getLocalServiceFieldName())).addModifier(Modifier.PUBLIC);
	}
	
	@Override
	public void finish(ServiceTypeElement serviceTypeElement) {
		List<ConverterConstructorParameter> params = mergeSameParams(converterParameters);

		MutableExecutableType constructor = serviceTypeElement.getServiceConverter().getConstructor();
		
		for (ConverterConstructorParameter converterParameter: params) {
			constructor.addParameter(processingEnv.getElementUtils().getParameterElement(
					converterParameter.getType(), converterParameter.getName()));
		}
	}
}