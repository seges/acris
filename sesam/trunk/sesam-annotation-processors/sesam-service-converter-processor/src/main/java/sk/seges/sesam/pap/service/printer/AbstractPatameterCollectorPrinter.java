package sk.seges.sesam.pap.service.printer;

import java.util.ArrayList;
import java.util.List;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterParameter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.api.ServiceConverterElementPrinter;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public abstract class AbstractPatameterCollectorPrinter extends AbstractServicePrinter implements ServiceConverterElementPrinter {

	protected List<ConverterParameter> converterParameters = new ArrayList<ConverterParameter>();
	protected final FormattedPrintWriter pw;
	
	protected AbstractPatameterCollectorPrinter(TransferObjectProcessingEnvironment processingEnv, ParametersResolver parametersResolver, FormattedPrintWriter pw) {
		super(processingEnv, parametersResolver);
		this.pw = pw;
	}
		
	@Override
	public void initialize(ServiceTypeElement serviceTypeElement, MutableDeclaredType outputName) {}

	@Override
	public void print(ServiceConverterPrinterContext context) {
		List<ConverterParameter> newParams = removeConverterParameters(getConverterParameters(context.getService(), context.getLocalServiceInterface()));
		converterParameters = unifyParameterNames(converterParameters, newParams);
	}

}
