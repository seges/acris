package sk.seges.sesam.pap.service.printer;

import java.util.List;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.accessor.AnnotationAccessor.AnnotationTypeFilter;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.printer.AnnotationPrinter;
import sk.seges.sesam.core.pap.printer.MethodPrinter;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.service.model.LocalServiceTypeElement;
import sk.seges.sesam.pap.service.model.RemoteServiceTypeElement;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.api.ServiceConverterElementPrinter;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public class ServiceMethodConverterPrinter extends AbstractServicePrinter implements ServiceConverterElementPrinter {

	protected final FormattedPrintWriter pw;
	private final ConverterProviderPrinter converterProviderPrinter;
	
	public ServiceMethodConverterPrinter(TransferObjectProcessingEnvironment processingEnv, ParametersResolver parametersResolver, 
			FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter) {
		super(processingEnv, parametersResolver);
		this.pw = pw;
		this.converterProviderPrinter = converterProviderPrinter;
	}

	@Override
	public void initialize(ServiceTypeElement serviceTypeElement, MutableDeclaredType outputName) {}

	protected void printCastLocalMethodResult(DtoType returnDtoType, ServiceConverterPrinterContext context) {}
	
	public static final String RESULT_VARIABLE_NAME = "result";
	
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


			DtoType returnDtoType = null;
			
			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
				returnDtoType = processingEnv.getTransferObjectUtils().getDtoType(remoteMethod.getReturnType());
			}

			new AnnotationPrinter(pw, processingEnv).printMethodAnnotations(localMethod, new AnnotationTypeFilter(true, getIgnoredAnnotations(localMethod)));

			new MethodPrinter(pw, processingEnv).printMethodDefinition(remoteMethod);
			
			pw.println("{");

			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
				
//				if (returnDtoType.getConverter() == null) {
//					pw.print("return ");
//				} else {
				pw.print(localMethod.getReturnType(), " " + RESULT_VARIABLE_NAME + " = ");
//				}
			}
			
			printCastLocalMethodResult(returnDtoType, context);
			
			pw.print(context.getLocalServiceFieldName() + "." + localMethod.getSimpleName().toString() + "(");

			for (int i = 0; i < localMethod.getParameters().size(); i++) {
				if (i > 0) {
					pw.print(", ");
				}

				TypeMirror dtoType = remoteMethod.getParameters().get(i).asType();
				
				DtoType parameterDtoType = processingEnv.getTransferObjectUtils().getDtoType(dtoType);
				DomainType parameterDomainType = parameterDtoType.getDomain();
				
				String parameterName = remoteMethod.getParameters().get(i).getSimpleName().toString();
				
				if (parameterDtoType.getConverter() != null) {
					pw.print("(", parameterDomainType, ")");
					converterProviderPrinter.printDtoConverterMethodName(parameterDtoType.getConverter(), parameterDtoType, parameterName, localMethod, pw);
					pw.print(".fromDto(");
				}

				pw.print(parameterName);

				if (parameterDtoType.getConverter() != null) {
					pw.print(")");
				}
			}

			pw.print(")");

			if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID) && returnDtoType.getConverter() != null) {
				pw.println(";");
				
				converterProviderPrinter.printConverterParams(returnDtoType.getConverter(), localMethod, pw);
				pw.print("return (", processingEnv.getTypeUtils().toMutableType(remoteMethod.getReturnType()), ")");
				converterProviderPrinter.printDomainConverterMethodName(returnDtoType.getConverter(), returnDtoType.getDomain(), RESULT_VARIABLE_NAME, localMethod, pw);
				pw.println(".toDto(" + RESULT_VARIABLE_NAME + ");");
			} else if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
				pw.println(";");
				pw.println("return " + RESULT_VARIABLE_NAME + ";");
			}
			
//			pw.println(";");
			pw.println("}");
			pw.println();
		}
	}

	@Override
	public void finish(ServiceTypeElement serviceTypeElement) {}
	
	protected Class<?>[] getIgnoredAnnotations(Element method) {
		return new Class<?>[] {
			SuppressWarnings.class
		};
	}
}