package sk.seges.sesam.pap.service.printer;

import java.util.List;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.service.model.LocalServiceTypeElement;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.api.ServiceConverterElementPrinter;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public abstract class AbstractServiceMethodPrinter extends AbstractServicePrinter implements ServiceConverterElementPrinter {

	protected final FormattedPrintWriter pw;
	protected final ConverterProviderPrinter converterProviderPrinter;
	
	public AbstractServiceMethodPrinter(TransferObjectProcessingEnvironment processingEnv, ConverterConstructorParametersResolver parametersResolver, 
			FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter) {
		super(processingEnv, parametersResolver);
		this.pw = pw;
		this.converterProviderPrinter = converterProviderPrinter;
	}

	@Override
	public void initialize(ServiceTypeElement serviceTypeElement, MutableDeclaredType outputName) {}

	@Override
	public void print(ServiceConverterPrinterContext context) {
		LocalServiceTypeElement localInterface = context.getLocalServiceInterface();
		ServiceTypeElement serviceTypeElement = context.getService();
		
		RemoteServiceTypeElement remoteServiceInterface = localInterface.getRemoteServiceInterface();

		if (remoteServiceInterface == null) {
			processingEnv.getMessager().printMessage(Kind.ERROR,
					"[ERROR] Unable to find remote service pair for the local service definition " + localInterface.toString(), serviceTypeElement.asElement());
			return;
		}

		List<ExecutableElement> remoteMethods = ElementFilter.methodsIn(remoteServiceInterface.asElement().getEnclosedElements());
		
		for (ExecutableElement remoteMethod : remoteMethods) {
			ExecutableElement localMethod = getDomainMethodPair(remoteMethod, serviceTypeElement);
			if (localMethod == null) {
				processingEnv.getMessager().printMessage(Kind.ERROR,
						"[ERROR] Service class does not implements local service method " + remoteMethod.toString()
								+ ". Please specify correct service implementation.", serviceTypeElement.asElement());
				continue;
			}
			
			handleMethod(context, localMethod, remoteMethod);
		}
	}

	protected abstract void handleMethod(ServiceConverterPrinterContext context, ExecutableElement localMethod, ExecutableElement remoteMethod);
	
	@Override
	public void finish(ServiceTypeElement serviceTypeElement) {}
}