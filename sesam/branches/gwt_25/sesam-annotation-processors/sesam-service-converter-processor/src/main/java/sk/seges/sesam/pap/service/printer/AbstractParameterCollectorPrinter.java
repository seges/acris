package sk.seges.sesam.pap.service.printer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import sk.seges.sesam.core.pap.model.ConverterParameter;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.service.model.ServiceConverterParametersFilter;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.api.ServiceConverterElementPrinter;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public abstract class AbstractParameterCollectorPrinter extends AbstractServicePrinter implements ServiceConverterElementPrinter {

	protected List<ConverterParameter> converterParameters = new ArrayList<ConverterParameter>();
	protected final FormattedPrintWriter pw;
	protected final ServiceConverterParametersFilter parametersFilter;
	
	protected AbstractParameterCollectorPrinter(TransferObjectProcessingEnvironment processingEnv, ServiceConverterParametersFilter parametersFilter, ConverterConstructorParametersResolver parametersResolver, FormattedPrintWriter pw) {
		super(processingEnv, parametersResolver);
		this.pw = pw;
		this.parametersFilter = parametersFilter;
	}
		
	@Override
	public void initialize(ServiceTypeElement serviceTypeElement, MutableDeclaredType outputName) {}

	@Override
	public void print(ServiceConverterPrinterContext context) {
		List<ConverterParameter> newParams = parametersFilter.getPropagatedParameters(getConverterParameters(context.getService(), context.getLocalServiceInterface()));
		converterParameters = unifyParameterNames(converterParameters, mergeSameParams(newParams));
		
		Collections.sort(converterParameters, new Comparator<ConverterParameter>() {

			@Override
			public int compare(ConverterParameter o1, ConverterParameter o2) {
				return o1.getName().compareTo(o2.getName());
			}
		});
	}
}