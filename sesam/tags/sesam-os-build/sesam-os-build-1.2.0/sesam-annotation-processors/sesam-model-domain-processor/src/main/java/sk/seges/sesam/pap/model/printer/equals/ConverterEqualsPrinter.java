package sk.seges.sesam.pap.model.printer.equals;

import java.util.Arrays;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.mutable.api.MutableArrayType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public class ConverterEqualsPrinter implements TransferObjectElementPrinter {

	private static final String DTO_ID = "dtoId";

	private final FormattedPrintWriter pw;
	private final ConverterProviderPrinter converterProviderPrinter;
	private final EntityResolver entityResolver;
	private final ProcessingEnvironment processingEnv;

	public ConverterEqualsPrinter(ConverterProviderPrinter converterProviderPrinter, EntityResolver entityResolver, ProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		this.pw = pw;
		this.entityResolver = entityResolver;
		this.processingEnv = processingEnv;
		this.converterProviderPrinter = converterProviderPrinter;
	}

	@Override
	public void initialize(ConfigurationTypeElement configurationTypeElement, MutableDeclaredType outputName) {
		pw.println("public boolean equals(", configurationTypeElement.getDomain(), " " + DOMAIN_NAME + ",",
											 configurationTypeElement.getDto(), " " + DTO_NAME + ") {");
		if (entityResolver.shouldHaveIdMethod(configurationTypeElement.getDomain())) {

			DomainType domainId = configurationTypeElement.getDomain().getId(entityResolver);

			if (domainId.getConverter() != null) {
				pw.print(domainId.getDto(), " " + DTO_ID + " = ");
				converterProviderPrinter.printDomainConverterMethodName(domainId.getConverter(), domainId, pw);
				pw.print(".toDto(");
			} else {
				pw.print(domainId, " " + DTO_ID + " = ");
			}

			pw.print(DOMAIN_NAME + "." + configurationTypeElement.getDomain().getIdMethod(entityResolver).getSimpleName().toString() + "()");

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
		
		if (entityResolver.shouldHaveIdMethod(context.getConfigurationTypeElement().getDomain())) {
			return;
		}
		
		if (context.getLocalConverterName() != null) {
			pw.println("if (!" + context.getLocalConverterName() + ".equals(" + DOMAIN_NAME + "." + context.getDomainFieldName() + "," +
					DTO_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + "))");
			pw.println("	return false;");
		} else {
			if (context.getFieldType() instanceof MutableArrayType) {
				pw.println("if (!" + Arrays.class.getCanonicalName() + ".equals(" + DOMAIN_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + ", " + DTO_NAME + "." + 
						MethodHelper.toGetter(context.getFieldName()) + "))");
				pw.println("	return false;");
				return;
			} 
			
			switch (context.getFieldType().getKind()) {
			case PRIMITIVE:
				pw.println("if (" + DOMAIN_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + " != " + DTO_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + ")");
				pw.println("	return false;");
				return;
			case WILDCARD:
			case VOID:
			case TYPEVAR:
				processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Unsupported type " + context.getFieldName() + " (" + context.getFieldType().getKind() + ") in the " + 
						context.getConfigurationTypeElement().asElement(), context.getConfigurationTypeElement().asElement());
				return;
			case CLASS:
			case INTERFACE:
				pw.println("if (" + DOMAIN_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + " == null) {");
				pw.println("if (" + DTO_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + " != null)");
				pw.println("	return false;");
				pw.println("} else if (!" + DOMAIN_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + ".equals(" + DTO_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + "))");
				pw.println("	return false;");
				return;

			case ENUM:
				pw.println("if (" + DOMAIN_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + " != " + DTO_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + ")");
				pw.println("	return false;");
				return;
			case ARRAY:
				pw.println("if (!" + Arrays.class.getCanonicalName() + ".equals(" + DOMAIN_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + ", " + DTO_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + "))");
				pw.println("	return false;");
				return;
			}
		}
	}

	@Override
	public void finish(ConfigurationTypeElement configurationTypeElement) {
		if (!entityResolver.shouldHaveIdMethod(configurationTypeElement.getDomain())) {
			pw.println("return true;");
		}
		pw.println("}");
		pw.println();
	}
}