package sk.seges.sesam.pap.service.printer;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;

import sk.seges.sesam.core.pap.model.ParameterElement;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ParametersFilter;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;
import sk.seges.sesam.pap.service.model.ServiceTypeElement;
import sk.seges.sesam.pap.service.printer.model.ServiceConverterPrinterContext;
import sk.seges.sesam.shared.model.converter.api.ConverterProvider;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class ServiceDomainConverterProviderPrinter extends AbstractServiceMethodPrinter{

	public ServiceDomainConverterProviderPrinter(TransferObjectProcessingEnvironment processingEnv,
			ParametersResolver parametersResolver, FormattedPrintWriter pw, ConverterProviderPrinter converterProviderPrinter) {
		super(processingEnv, parametersResolver, pw, converterProviderPrinter);
	}

	private MutableDeclaredType getTypedDtoConverter() {
		MutableTypes typeUtils = processingEnv.getTypeUtils();
		return typeUtils.getDeclaredType(typeUtils.toMutableType(DtoConverter.class), 
				typeUtils.getTypeVariable(ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX), 
				typeUtils.getTypeVariable(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX));
	}

	private static final String DOMAIN_PARAMETER_NAME = "domain";
	private static final String DTO_PARAMETER_NAME = "dto";

	private static final String CONVERTER_PROVIDER_SUFFIX = "ConverterProvider";
	
	@Override
	public void initialize(ServiceTypeElement serviceTypeElement, MutableDeclaredType outputName) {
		types.clear();

		MutableDeclaredType converterProviderType = processingEnv.getTypeUtils().toMutableType(ConverterProvider.class);
		
		pw.print("class " + serviceTypeElement.getSimpleName() + CONVERTER_PROVIDER_SUFFIX + " implements ");
		pw.println(converterProviderType, " {");
		pw.println();
		
		ParameterElement[] generatedParameters = ParametersFilter.NOT_PROPAGATED.filterParameters(parametersResolver.getConstructorAditionalParameters());

		List<ParameterElement> filteredParameters = new ArrayList<ParameterElement>();
		for (ParameterElement generatedParameter: generatedParameters) {
			if (!generatedParameter.getType().equals(converterProviderType)) {
				filteredParameters.add(generatedParameter);
			}
		}
		
		generatedParameters = filteredParameters.toArray(new ParameterElement[] {});
		
		for (ParameterElement generatedParameter: generatedParameters) {
			pw.println("private final ", generatedParameter.getType(), " " + generatedParameter.getName() + ";");
			pw.println();
		}
		
		pw.print(serviceTypeElement.getSimpleName() + CONVERTER_PROVIDER_SUFFIX + "(");
		
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
		pw.println("public <" + ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + ", " + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX +
				"> ", getTypedDtoConverter(), " getConverterForDomain(" + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + " " + DOMAIN_PARAMETER_NAME + ") {");
	}
	
//	protected void printDtoConverterProviderMethod() {
//		pw.println("<" + ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + ", " + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX +
//				">", getTypedDtoConverter(), " getConverterForDto(" + ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + " " + DTO_PARAMETER_NAME + ") {");
//		pw.println("}");
//	}

	private Set<String> types = new HashSet<String>();
	
	private void printDomainTypeConverter(DomainDeclaredType rawDomain, ConverterTypeElement converterType, ExecutableElement localMethod) {
		if (!types.contains(rawDomain.getCanonicalName())) {
			types.add(rawDomain.getCanonicalName());
			pw.println("if (" + DOMAIN_PARAMETER_NAME + " instanceof ", rawDomain, ") {");
			String fieldName = MethodHelper.toField(rawDomain.getSimpleName());
			pw.print(rawDomain, " " + fieldName + " = (", rawDomain, ")" + DOMAIN_PARAMETER_NAME + ";");
			pw.print("return (", getTypedDtoConverter(), ") ");
			converterProviderPrinter.printDomainConverterMethodName(converterType, rawDomain, fieldName, localMethod, pw);
			pw.println(";");
			pw.println("}");
			pw.println();
		}
	}
	
	@Override
	protected void handleMethod(ServiceConverterPrinterContext context, ExecutableElement localMethod, ExecutableElement remoteMethod) {
		if (!remoteMethod.getReturnType().getKind().equals(TypeKind.VOID)) {
			DtoType returnDtoType = processingEnv.getTransferObjectUtils().getDtoType(remoteMethod.getReturnType());

			if (returnDtoType.getConverter() != null) {
				DomainDeclaredType rawDomain = returnDtoType.getConverter().getConfiguration().getRawDomain();
				printDomainTypeConverter(rawDomain, returnDtoType.getConverter(), localMethod);
			}
		}

		for (int i = 0; i < localMethod.getParameters().size(); i++) {
			TypeMirror dtoType = remoteMethod.getParameters().get(i).asType();
			DtoType parameterDtoType = processingEnv.getTransferObjectUtils().getDtoType(dtoType);
			if (parameterDtoType.getConverter() != null) {
				printDomainTypeConverter(parameterDtoType.getConverter().getConfiguration().getRawDomain(), 
						parameterDtoType.getConverter(), localMethod);
			}
		}
	}
	
	@Override
	public void finish(ServiceTypeElement serviceTypeElement) {
		pw.println("return null;");
		pw.println("}");
		pw.println();
		pw.println("}");
	}
}