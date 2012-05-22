package sk.seges.sesam.pap.model.printer.equals;

import java.util.Arrays;

import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.mutable.api.MutableArrayType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.printer.AbstractDtoPrinter;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterTargetType;
import sk.seges.sesam.pap.model.resolver.api.ConverterConstructorParametersResolver;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public class ConverterEqualsPrinter extends AbstractDtoPrinter implements TransferObjectElementPrinter {

	private static final String DTO_ID = "dtoId";

	private final ConverterProviderPrinter converterProviderPrinter;
	private final EntityResolver entityResolver;
	private final FormattedPrintWriter pw;

	public ConverterEqualsPrinter(ConverterProviderPrinter converterProviderPrinter, EntityResolver entityResolver, ConverterConstructorParametersResolver parametersResolver, TransferObjectProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(parametersResolver, processingEnv);
		this.entityResolver = entityResolver;
		this.converterProviderPrinter = converterProviderPrinter;
		this.pw = pw;
	}

	@Override
	public void initialize(ConfigurationTypeElement configurationTypeElement, MutableDeclaredType outputName) {
		pw.println("public boolean equals(", configurationTypeElement.getDomain(), " " + DOMAIN_NAME + ", ",
											 configurationTypeElement.getDto(), " " + DTO_NAME + ") {");
		if (entityResolver.shouldHaveIdMethod(configurationTypeElement.getInstantiableDomain())) {

			DomainType domainId = configurationTypeElement.getInstantiableDomain().getId(entityResolver);

			String methodName = DOMAIN_NAME + "." + configurationTypeElement.getInstantiableDomain().getIdMethod(entityResolver).getSimpleName().toString() + "()";
			
			if (domainId.getConverter() != null) {
				pw.print(domainId.getDto(), " " + DTO_ID + " = ");
				converterProviderPrinter.printDomainEnsuredConverterMethodName(domainId, null, methodName, configurationTypeElement.getInstantiableDomain().getIdMethod(entityResolver), pw, false);
				pw.print(".toDto(");
			} else {
				pw.print(domainId, " " + DTO_ID + " = ");
			}

			pw.print(methodName);

			if (domainId.getConverter() != null) {
				pw.print(")");
			}

			pw.println(";");
			pw.println("if (" + DTO_ID + " == null) {");
			pw.println("return false;");
			pw.println("}");
			pw.println();
			pw.println("return " + DTO_ID + ".equals(" + DTO_NAME + "."
					+ configurationTypeElement.getDto().getIdMethod(entityResolver).getSimpleName() + "());");
		}
	}

	@Override
	public void print(TransferObjectContext context) {
		
		if (entityResolver.shouldHaveIdMethod(context.getConfigurationTypeElement().getInstantiableDomain())) {
			return;
		}
		
		if (context.isLocalConverter()) {
			String localConverter = printLocalConverter(context, ConverterTargetType.DOMAIN, pw);
			pw.println("if (!" + localConverter + ".equals(" + DOMAIN_NAME + "." + context.getDomainFieldName() + "," +
					DTO_NAME + "." + MethodHelper.toGetter(context.getDtoFieldName()) + "))");
			pw.println("	return false;");
		} else {
			if (context.getDtoFieldType() instanceof MutableArrayType) {
				pw.println("if (!" + Arrays.class.getCanonicalName() + ".equals(" + DOMAIN_NAME + "." + context.getDomainFieldName() + ", " + DTO_NAME + "." + 
						MethodHelper.toGetter(context.getDtoFieldName()) + "))");
				pw.println("	return false;");
				return;
			} 
			
			switch (context.getDtoFieldType().getKind()) {
			case PRIMITIVE:
				pw.println("if (" + DOMAIN_NAME + "." + context.getDomainFieldName() + " != " + DTO_NAME + "." + MethodHelper.toGetter(context.getDtoFieldName()) + ")");
				pw.println("	return false;");
				return;
			case WILDCARD:
			case VOID:
			case TYPEVAR:
				processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Unsupported type " + context.getDtoFieldName() + " (" + context.getDtoFieldType().getKind() + ") in the " + 
						context.getConfigurationTypeElement().asConfigurationElement(), context.getConfigurationTypeElement().asConfigurationElement());
				return;
			case CLASS:
			case INTERFACE:
				pw.println("if (" + DOMAIN_NAME + "." + context.getDomainFieldName() + " == null) {");
				pw.println("if (" + DTO_NAME + "." + MethodHelper.toGetter(context.getDtoFieldName()) + " != null)");
				pw.println("	return false;");
				pw.println("} else if (!" + DOMAIN_NAME + "." + context.getDomainFieldName() + ".equals(" + DTO_NAME + "." + MethodHelper.toGetter(context.getDtoFieldName()) + "))");
				pw.println("	return false;");
				return;

			case ENUM:
				pw.println("if (" + DOMAIN_NAME + "." + context.getDomainFieldName() + " != " + DTO_NAME + "." + MethodHelper.toGetter(context.getDtoFieldName()) + ")");
				pw.println("	return false;");
				return;
			case ARRAY:
				pw.println("if (!" + Arrays.class.getCanonicalName() + ".equals(" + DOMAIN_NAME + "." + context.getDomainFieldName() + ", " + DTO_NAME + "." + MethodHelper.toGetter(context.getDtoFieldName()) + "))");
				pw.println("	return false;");
				return;
			}
		}
	}

	@Override
	public void finish(ConfigurationTypeElement configurationTypeElement) {
		if (!entityResolver.shouldHaveIdMethod(configurationTypeElement.getInstantiableDomain())) {
			pw.println("return true;");
		}
		pw.println("}");
		pw.println();
	}
}