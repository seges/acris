package sk.seges.sesam.pap.service.printer;

import javax.lang.model.element.Modifier;

import sk.seges.sesam.core.pap.model.ConverterConstructorParameter;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableVariableElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.service.model.ServiceConverterParametersFilter;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;

public class ConverterParameterFieldPrinter extends AbstractParameterCollectorPrinter {
		
	public ConverterParameterFieldPrinter(TransferObjectProcessingEnvironment processingEnv, ServiceConverterParametersFilter parametersFilter, 
			ConverterConstructorParametersResolverProvider parametersResolverProvider) {
		super(processingEnv, parametersFilter, parametersResolverProvider);
	}

	@Override
	public void finish(ServiceTypeElement serviceTypeElement) {
		for (ConverterConstructorParameter converterParameter: converterParameters) {
			serviceTypeElement.getServiceConverter().addField((MutableVariableElement) processingEnv.getElementUtils().getParameterElement(
					converterParameter.getType(), converterParameter.getName()).addModifier(Modifier.PROTECTED));
		}
	}	
}