package sk.seges.sesam.pap.service.printer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sk.seges.sesam.core.pap.model.ConverterConstructorParameter;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.service.model.ServiceConverterParametersFilter;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.api.ServiceConverterElementPrinter;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public abstract class AbstractParameterCollectorPrinter extends AbstractServicePrinter implements ServiceConverterElementPrinter {

	protected List<ConverterConstructorParameter> converterParameters = new ArrayList<ConverterConstructorParameter>();
	protected final ServiceConverterParametersFilter parametersFilter;
	
	protected AbstractParameterCollectorPrinter(TransferObjectProcessingEnvironment processingEnv, ServiceConverterParametersFilter parametersFilter, 
			ConverterConstructorParametersResolverProvider parametersResolverProvider) {
		super(processingEnv, parametersResolverProvider);
		this.parametersFilter = parametersFilter;
	}
		
	@Override
	public void initialize(ServiceTypeElement serviceTypeElement, MutableDeclaredType outputName) {}

	@Override
	public void print(ServiceConverterPrinterContext context) {
		List<ConverterConstructorParameter> newParams = parametersFilter.getPropagatedParameters(getConverterParameters(context.getService(), context.getLocalServiceInterface()));
		converterParameters.addAll(mergeSameParams(newParams));
		converterParameters = unifyParameterNames(new ArrayList<ConverterConstructorParameter>(), mergeSameParams(converterParameters));
		
		Collections.sort(converterParameters, new Comparator<ConverterConstructorParameter>() {

			@Override
			public int compare(ConverterConstructorParameter o1, ConverterConstructorParameter o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}
}