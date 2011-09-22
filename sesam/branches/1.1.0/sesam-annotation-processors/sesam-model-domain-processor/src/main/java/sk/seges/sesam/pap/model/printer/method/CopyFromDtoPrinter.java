package sk.seges.sesam.pap.model.printer.method;

import java.io.Serializable;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.model.DtoTypeElement;
import sk.seges.sesam.pap.model.printer.api.ElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class CopyFromDtoPrinter extends AbstractMethodPrinter implements ElementPrinter {

	private final FormattedPrintWriter pw;
	private EntityResolver entityResolver;
	
	public CopyFromDtoPrinter(ConverterProviderPrinter converterProviderPrinter, EntityResolver entityResolver, ParametersResolver parametersResolver, RoundEnvironment roundEnv, ProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(converterProviderPrinter, parametersResolver, roundEnv, processingEnv);
		this.pw = pw;
		this.entityResolver = entityResolver;
	}
	
	@Override
	public void print(ProcessorContext context) {
		copy(context, pw, new CopyFromDtoMethodPrinter(converterProviderPrinter, entityResolver, parametersResolver, roundEnv, processingEnv));
	}

	@Override
	public void initialize(ConfigurationTypeElement configurationElement, NamedType outputName) {
		
		ImmutableType dtoType = getDtoType(configurationElement);
		ImmutableType domainType = getDomainType(configurationElement);
				
		DomainTypeElement domainTypeElement = configurationElement.getDomain();

		ExecutableElement idMethod = null;
		
		if (domainTypeElement.asType().getKind().equals(TypeKind.DECLARED)) {
			idMethod = domainTypeElement.getIdMethod(entityResolver);
			
			if (idMethod == null && entityResolver.shouldHaveIdMethod(domainTypeElement)) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method for " + configurationElement.toString(), configurationElement.asElement());
				return;
			}
		}
		
		pw.println("public ", domainType, " createDomainInstance(", Serializable.class, " id) {");
		
		printDomainInstancer(pw, domainType);
		pw.println("}");
		pw.println();

		pw.println("public ", domainType, " fromDto(", dtoType, " " + DTO_NAME + ") {");
		pw.println();
		pw.println("if (" + DTO_NAME + " == null) {");
		pw.println("return null;");
		pw.println("}");
		pw.println();
		
		ExecutableElement dtoIdMethod = configurationElement.getDto().getIdMethod(entityResolver);
		
		if (dtoIdMethod == null && entityResolver.shouldHaveIdMethod(domainTypeElement)) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method for DTO class " + dtoType.getCanonicalName(), configurationElement.asElement());
			return;
		}
		
		if (dtoIdMethod == null) {
			//TODO potential cycle
			pw.println(domainType, " " + RESULT_NAME + " = createDomainInstance(null);");
		} else {
			
			boolean useIdConverter = false;

			NamedType dtoIdType = nameTypesUtils.toType(idMethod.getReturnType());
			DomainTypeElement domainIdType = null;
			
			if (idMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
				domainIdType = domainTypeElement.getId(entityResolver);
				if (domainIdType.getConfiguration() != null) {
					DtoTypeElement dto = domainIdType.getConfiguration().getDto();
					if (dto != null) {
						dtoIdType= dto;
					}
				}
			}

			pw.println(domainType, " " + RESULT_NAME + " = getDomainInstance(" + DTO_NAME + ", " + DTO_NAME + "." + MethodHelper.toGetter(MethodHelper.toField(dtoIdMethod)) + ");");
			pw.println("if (" + RESULT_NAME + " != null) {");
			pw.println("return " + RESULT_NAME + ";");
			pw.println("}");

			String idName = "_id";
			
			if (domainIdType != null) {
				pw.print(domainIdType, " " + idName + " = ");
			} else {
				//Types are the same
				pw.print(dtoIdType, " " + idName + " = ");
			}
			
			if (idMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
				ConfigurationTypeElement idConfigurationElement = domainIdType.getConfiguration();
					//toHelper.getConfigurationElement(domainIdType, roundEnv);
				if (idConfigurationElement != null && idConfigurationElement.getConverter() != null) {
					converterProviderPrinter.printDomainConverterMethodName(idConfigurationElement.getConverter(), domainIdType.asType(), pw);
					pw.print(".fromDto(");
					useIdConverter = true;
				}
			}

			pw.print(DTO_NAME + "." + MethodHelper.toGetter(MethodHelper.toField(dtoIdMethod)));

			if (useIdConverter) {
				pw.print(")");
			}
			pw.println(";");
			pw.println();

			pw.println(RESULT_NAME + " = createDomainInstance(" + DTO_NAME + ", " + idName + ");");
			pw.println();
		}
		
		pw.println();
		pw.println("return convertFromDto(" + RESULT_NAME + ", " + DTO_NAME + ");");
		pw.println("}");
		pw.println();
		pw.println("public ", domainType, " convertFromDto(", domainType, " " + RESULT_NAME + ", ", dtoType, " " + DTO_NAME + ") {");
		pw.println();
		pw.println("if (" + DTO_NAME + "  == null) {");
		pw.println("return null;");
		pw.println("}");
		pw.println();

		DomainTypeElement domainsuperClass = configurationElement.getDomain().getSuperClass();
		
		if (domainsuperClass != null && domainsuperClass.getConfiguration().getConverter() != null) {
			converterProviderPrinter.printDomainConverterMethodName(domainsuperClass.getConfiguration().getConverter(), domainsuperClass.asType(), pw);
			pw.println(".convertFromDto(" + RESULT_NAME + ", " + DTO_NAME + ");");
			pw.println();
		}
	}
	
	@Override
	public void finish(ConfigurationTypeElement configuratioTypeElement) {
		pw.println("return " + RESULT_NAME + ";");
		pw.println("}");
		pw.println();
	}

	protected void printDomainInstancer(FormattedPrintWriter pw, NamedType type) {
		pw.println("return new ", type, "();");
	}
}