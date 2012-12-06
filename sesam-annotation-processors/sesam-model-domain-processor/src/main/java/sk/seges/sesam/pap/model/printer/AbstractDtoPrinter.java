package sk.seges.sesam.pap.model.printer;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.model.mutable.utils.MutableTypes;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.AbstractConverterPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterTargetType;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.DefaultConverterConstructorParametersResolver;
import sk.seges.sesam.shared.model.converter.ConvertedInstanceCache;
import sk.seges.sesam.shared.model.converter.api.DtoConverter;

public class AbstractDtoPrinter extends AbstractConverterPrinter {

	protected AbstractDtoPrinter(ConverterConstructorParametersResolverProvider parametersResolverProvider, TransferObjectProcessingEnvironment processingEnv) {
		super(parametersResolverProvider, processingEnv);
	}
	
	protected String printLocalConverter(TransferObjectContext context, ConverterTargetType targetType, FormattedPrintWriter pw) {
		MutableTypes typeUtils = processingEnv.getTypeUtils();
		
		MutableDeclaredType dtoConverter = typeUtils.toMutableType(DtoConverter.class);
		
		DtoType dto = context.getDomainMethodReturnType().getDto();
		
		MutableTypeVariable dtoType = (MutableTypeVariable)dto;
//		dtoType = dtoType.clone().setVariable(ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX + "_" + dtoType.getVariable());
		MutableTypeVariable domainType = (MutableTypeVariable)context.getDomainMethodReturnType();
		domainType = domainType.clone().setVariable(ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "_" + domainType.getVariable());
		
		dtoConverter = dtoConverter.setTypeVariables(dtoType, domainType);
		String converterName = "converter" + MethodHelper.toMethod(MethodHelper.toField(context.getDomainMethod()));
		pw.print(dtoConverter, " " + converterName + " = (", dtoConverter, ")");

		String cacheParameterName = getConstructorParameterName(processingEnv.getTypeUtils().toMutableType(ConvertedInstanceCache.class));
		
		if (targetType.equals(ConverterTargetType.DOMAIN)) {
			pw.print(DefaultConverterConstructorParametersResolver.CONVERTER_PROVIDER_NAME + ".getConverterForDomain(" + TransferObjectElementPrinter.DOMAIN_NAME  + "." + context.getDomainFieldName());
		} else {
			pw.print(DefaultConverterConstructorParametersResolver.CONVERTER_PROVIDER_NAME + ".getConverterForDomain(" + TransferObjectElementPrinter.RESULT_NAME  + "." + context.getDomainFieldName());
		}
		if (cacheParameterName != null) {
			pw.print(", " + cacheParameterName);
		}
		pw.println(");");
		return converterName;
	}
}
