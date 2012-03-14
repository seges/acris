package sk.seges.sesam.pap.service.printer;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.printer.TypePrinter;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.service.model.ServiceConvertProviderType;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.api.NestedServiceConverterElementPrinter;
import sk.seges.sesam.pap.service.printer.model.NestedServiceConverterPrinterContext;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;

public class ServiceConverterProviderPrinter extends AbstractServiceMethodPrinter {

	public ServiceConverterProviderPrinter(TransferObjectProcessingEnvironment processingEnv,
			ParametersResolver parametersResolver, FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter) {
		super(processingEnv, parametersResolver, pw, converterProviderPrinter);
	}
	
	@Override
	public void initialize(ServiceTypeElement serviceTypeElement, MutableDeclaredType outputName) {}

	protected void initialize(ServiceTypeElement serviceTypeElement) {
		
		ServiceConvertProviderType serviceConvertProviderType = new ServiceConvertProviderType(serviceTypeElement, processingEnv);
		
		new TypePrinter(pw).printTypeDefinition(Modifier.PROTECTED, serviceConvertProviderType);
		pw.println(" {");
		pw.println();
		
		ParameterElement[] generatedParameters = serviceConvertProviderType.getConverterParameters(parametersResolver);
		
		for (ParameterElement generatedParameter: generatedParameters) {
			pw.println(Modifier.PROTECTED.toString() + " " + Modifier.FINAL.toString() + " ", generatedParameter.getType(), " " + generatedParameter.getName() + ";");
			pw.println();
		}
		
		pw.print(Modifier.PROTECTED.toString() + " ", serviceTypeElement.getSimpleName() + ServiceConvertProviderType.CONVERTER_PROVIDER_SUFFIX + "(");
		
		int i = 0;
		for (ParameterElement generatedParameter: generatedParameters) {
			if (i > 0) {
				pw.print(", ");
			}
			pw.print(generatedParameter.getType(), " " + generatedParameter.getName());
			i++;
		}
		
		pw.println(") {");

		for (ParameterElement generatedParameter: generatedParameters) {
			pw.println("this." + generatedParameter.getName() + " = " + generatedParameter.getName() + ";");
		}
		
		pw.println("}");
		pw.println();
	}
	
	protected NestedServiceConverterElementPrinter[] getNestedPrinters() {
		return new NestedServiceConverterElementPrinter[] {
			new ServiceDomainConverterProviderPrinter(processingEnv, pw, converterProviderPrinter),
			new ServiceDtoConverterProviderPrinter(processingEnv, pw, converterProviderPrinter)	
		};
	}
	
	@Override
	public void print(ServiceConverterPrinterContext context) {
		ConverterVerifier converterVerifier = new ConverterVerifier();
		context.setNestedPrinter(converterVerifier);
		super.print(context);
		
		if (converterVerifier.isContainsConverter()) {
			initialize(context.getService());
			for (NestedServiceConverterElementPrinter nestedPrinter: getNestedPrinters()) {
				nestedPrinter.initialize();
				context.setNestedPrinter(nestedPrinter);
				super.print(context);
				nestedPrinter.finish();
			}
			pw.println("}");
		}
	}
	
	@Override
	protected void handleMethod(ServiceConverterPrinterContext context, ExecutableElement localMethod, ExecutableElement remoteMethod) {
		NestedServiceConverterElementPrinter nestedPrinter = context.getNestedPrinter();
		if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
			DtoType returnDtoType = processingEnv.getTransferObjectUtils().getDtoType(remoteMethod.getReturnType());

			if (returnDtoType.getConverter() != null) {
				DomainDeclaredType rawDomain = (DomainDeclaredType)returnDtoType.getDomain();
				//returnDtoType.getConverter().getConfiguration().getRawDomain();
				DtoDeclaredType rawDto = returnDtoType.getConverter().getConfiguration().getRawDto();
				nestedPrinter.print(new NestedServiceConverterPrinterContext(rawDomain, rawDto, returnDtoType.getConverter(), localMethod));
			}
		}

		for (int i = 0; i < localMethod.getParameters().size(); i++) {
			TypeMirror dtoType = remoteMethod.getParameters().get(i).asType();
			DtoType parameterDtoType = processingEnv.getTransferObjectUtils().getDtoType(dtoType);
			if (parameterDtoType.getConverter() != null) {
				nestedPrinter.print(new NestedServiceConverterPrinterContext(parameterDtoType.getConverter().getConfiguration().getRawDomain(), 
						parameterDtoType.getConverter().getConfiguration().getRawDto(), parameterDtoType.getConverter(), localMethod));
			}
		}
	}
	
	@Override
	public void finish(ServiceTypeElement serviceTypeElement) {
	}
}