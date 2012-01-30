package sk.seges.sesam.pap.service.printer;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.accessor.AnnotationAccessor.AnnotationTypeFilter;
import sk.seges.sesam.core.pap.printer.AnnotationPrinter;
import sk.seges.sesam.core.pap.printer.MethodPrinter;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public class ServiceMethodConverterPrinter extends AbstractServiceMethodPrinter {

	public ServiceMethodConverterPrinter(TransferObjectProcessingEnvironment processingEnv,
			ParametersResolver parametersResolver, FormattedPrintWriter pw,
			ConverterProviderPrinter converterProviderPrinter) {
		super(processingEnv, parametersResolver, pw, converterProviderPrinter);
	}

	protected void printCastLocalMethodResult(DtoType returnDtoType, ServiceConverterPrinterContext context) {}
	
	public static final String RESULT_VARIABLE_NAME = "result";
	
	protected void handleMethod(ServiceConverterPrinterContext context, ExecutableElement localMethod, ExecutableElement remoteMethod) {

		DtoType returnDtoType = null;
		
		if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
			returnDtoType = processingEnv.getTransferObjectUtils().getDtoType(remoteMethod.getReturnType());
		}

		new AnnotationPrinter(pw, processingEnv).printMethodAnnotations(localMethod, new AnnotationTypeFilter(true, getIgnoredAnnotations(localMethod)));

		new MethodPrinter(pw, processingEnv).printMethodDefinition(remoteMethod);
		
		pw.println("{");

		boolean hasConverter = false;
		
		if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
			if (returnDtoType.getConverter() != null) {
				hasConverter = true;
			}
		}
		
		for (int i = 0; i < localMethod.getParameters().size(); i++) {
			TypeMirror dtoType = remoteMethod.getParameters().get(i).asType();
			DtoType parameterDtoType = processingEnv.getTransferObjectUtils().getDtoType(dtoType);
			
			if (parameterDtoType.getConverter() != null) {
				hasConverter = true;
				break;
			}
		}

		if (hasConverter) {
			converterProviderPrinter.printConverterParams(localMethod, pw);
		}
		
		if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
			pw.print(localMethod.getReturnType(), " " + RESULT_VARIABLE_NAME + " = ");
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
		pw.println(";");

		if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID) && returnDtoType.getConverter() != null) {
			pw.print("return (", processingEnv.getTypeUtils().toMutableType(remoteMethod.getReturnType()), ")");
			converterProviderPrinter.printDomainConverterMethodName(returnDtoType.getConverter(), 
					returnDtoType, RESULT_VARIABLE_NAME, localMethod, pw);
			pw.println(".toDto(" + RESULT_VARIABLE_NAME + ");");
		} else if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
			pw.println("return " + RESULT_VARIABLE_NAME + ";");
		}
		
		pw.println("}");
		pw.println();
	}

	protected Class<?>[] getIgnoredAnnotations(Element method) {
		return new Class<?>[] {
			SuppressWarnings.class
		};
	}
}