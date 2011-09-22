package sk.seges.sesam.pap.model.printer.equals;

import java.util.Arrays;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.type.DeclaredType;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.builder.NameTypeUtils;
import sk.seges.sesam.core.pap.model.api.ArrayNamedType;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.TypeParametersSupport;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.model.DtoTypeElement;
import sk.seges.sesam.pap.model.printer.api.ElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public class ConverterEqualsPrinter implements ElementPrinter {

	private static final String DTO_ID = "dtoId";

	private final FormattedPrintWriter pw;
	private final ConverterProviderPrinter converterProviderPrinter;
	private final EntityResolver entityResolver;
	private final ProcessingEnvironment processingEnv;
	private final TypeParametersSupport typeParametersSupport;

	public ConverterEqualsPrinter(ConverterProviderPrinter converterProviderPrinter, EntityResolver entityResolver, ProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		this.pw = pw;
		this.entityResolver = entityResolver;
		this.processingEnv = processingEnv;
		this.converterProviderPrinter = converterProviderPrinter;
		this.typeParametersSupport = new TypeParametersSupport(processingEnv, new NameTypeUtils(processingEnv));
	}

	@Override
	public void initialize(ConfigurationTypeElement configurationTypeElement, NamedType outputName) {
		pw.println("public boolean equals(", getDomainType(configurationTypeElement), " " + DOMAIN_NAME + ",",
											 getDtoType(configurationTypeElement), " " + DTO_NAME + ") {");
		if (entityResolver.shouldHaveIdMethod(configurationTypeElement.getDomain())) {

			DomainTypeElement domainId = configurationTypeElement.getDomain().getId(entityResolver);

			if (domainId.getConverter() != null) {
				pw.print(domainId.getDtoTypeElement(), " " + DTO_ID + " = ");
				converterProviderPrinter.printDomainConverterMethodName(domainId.getConverter(), domainId.asType(), pw);
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

	//TODO move to the configuration type element
	private ImmutableType getDtoType(ConfigurationTypeElement configurationElement) {
		DtoTypeElement dtoType = configurationElement.getDto();
		
		if (dtoType != null) {
			return typeParametersSupport.prefixTypeParameter(dtoType, ConverterTypeElement.DTO_TYPE_ARGUMENT_PREFIX);
		}
		return null;
	}

	//TODO move to the configuration type element
	private ImmutableType getDomainType(ConfigurationTypeElement configurationElement) {
		DomainTypeElement domainType = configurationElement.getDomain();
		
		if (domainType != null) {
			return typeParametersSupport.prefixTypeParameter(domainType, ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX);
		}
		
		return null;
	}
	@Override
	public void print(ProcessorContext context) {
		
		if (entityResolver.shouldHaveIdMethod(context.getConfigurationTypeElement().getDomain())) {
			return;
		}
		
		if (context.getLocalConverterName() != null) {
			pw.println("if (!" + context.getLocalConverterName() + ".equals(" + DOMAIN_NAME + "." + context.getDomainFieldName() + "," +
					DTO_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + "))");
			pw.println("	return false;");
		} else {
			if (context.getFieldType() instanceof ArrayNamedType) {
				pw.println("if (!" + Arrays.class.getCanonicalName() + ".equals(" + DOMAIN_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + ", " + DTO_NAME + "." + 
						MethodHelper.toGetter(context.getFieldName()) + "))");
				pw.println("	return false;");
				return;
			} 
			
			if (context.getFieldType().asType() == null) {
				pw.println("if (" + DOMAIN_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + " == null) {");
				pw.println("if (" + DTO_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + " != null)");
				pw.println("	return false;");
				pw.println("} else if (!" + DOMAIN_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + ".equals(" + DTO_NAME + "." + 
						MethodHelper.toGetter(context.getFieldName()) + ")) ");
				pw.println("	return false;");
				return;
			}

			switch (context.getFieldType().asType().getKind()) {
			case BOOLEAN:
			case BYTE:
			case CHAR:
			case DOUBLE:
			case FLOAT:
			case INT:
			case LONG:
			case SHORT:
				pw.println("if (" + DOMAIN_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + " != " + DTO_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + ")");
				pw.println("	return false;");
				return;
			case EXECUTABLE:
			case NONE:
			case NULL:
			case OTHER:
			case PACKAGE:
			case ERROR:
			case WILDCARD:
			case VOID:
			case TYPEVAR:
				processingEnv.getMessager().printMessage(Kind.WARNING, "[WARNING] Unsupported type " + context.getFieldName() + " (" + context.getFieldType().asType().getKind() + ") in the " + 
						context.getConfigurationTypeElement().asElement(), context.getConfigurationTypeElement().asElement());
				return;
			case DECLARED:
				Element element = ((DeclaredType)context.getFieldType().asType()).asElement();
				switch (element.getKind()) {
				case ENUM:
				case ENUM_CONSTANT:
					pw.println("if (" + DOMAIN_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + " != " + DTO_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + ")");
					pw.println("	return false;");
					return;
				case CLASS:
				case INTERFACE:
					pw.println("if (" + DOMAIN_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + " == null) {");
					pw.println("if (" + DTO_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + " != null)");
					pw.println("	return false;");
					pw.println("} else if (!" + DOMAIN_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + ".equals(" + DTO_NAME + "." + MethodHelper.toGetter(context.getFieldName()) + "))");
					pw.println("	return false;");
					return;
				}
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