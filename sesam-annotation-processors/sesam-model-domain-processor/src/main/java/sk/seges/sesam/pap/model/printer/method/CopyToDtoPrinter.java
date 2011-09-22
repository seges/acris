package sk.seges.sesam.pap.model.printer.method;

import java.io.PrintWriter;
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
import sk.seges.sesam.pap.model.model.api.ElementHolderTypeConverter;
import sk.seges.sesam.pap.model.printer.api.ElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class CopyToDtoPrinter extends AbstractMethodPrinter implements ElementPrinter {

	private final FormattedPrintWriter pw;

	private ElementHolderTypeConverter elementHolderTypeConverter;
	private EntityResolver entityResolver;
	
	public CopyToDtoPrinter(ConverterProviderPrinter converterProviderPrinter, ElementHolderTypeConverter elementHolderTypeConverter, EntityResolver entityResolver, ParametersResolver parametersResolver, 
			RoundEnvironment roundEnv, ProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(converterProviderPrinter, parametersResolver, roundEnv, processingEnv);
		this.pw = pw;
		this.elementHolderTypeConverter = elementHolderTypeConverter;
		this.entityResolver = entityResolver;
	}
	
	@Override
	public void print(ProcessorContext context) {
		copy(context, pw, new CopyToDtoMethodPrinter(converterProviderPrinter, elementHolderTypeConverter, entityResolver, parametersResolver, roundEnv, processingEnv));
	}

	@Override
	public void initialize(ConfigurationTypeElement configurationElement, NamedType outputName) {
		
		ImmutableType dtoType = getDtoType(configurationElement);
		ImmutableType domainType = getDomainType(configurationElement);
		
		DomainTypeElement domainTypeElement = configurationElement.getDomain();

		String instanceName = "instance";
		
		pw.println("protected boolean isInitialized(", Object.class, " " + instanceName + ") {");
		printIsInitializedMethod(pw, instanceName);
		pw.println("}");
		pw.println();
		
		pw.println("public ", dtoType, " createDtoInstance(", Serializable.class, " id) {");
		printDtoInstancer(pw, dtoType);
		pw.println("}");
		pw.println();
							
		pw.println("public ", dtoType, " toDto(", domainType, " " + DOMAIN_NAME + ") {");
		pw.println();
		pw.println("if (" + DOMAIN_NAME + "  == null) {");
		pw.println("return null;");
		pw.println("}");
		pw.println();

		ExecutableElement idMethod = null;
		
		if (domainType.asType().getKind().equals(TypeKind.DECLARED)) {
			idMethod = domainTypeElement.getIdMethod(entityResolver);
			
			if (idMethod == null && entityResolver.shouldHaveIdMethod(domainTypeElement)) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method for " + configurationElement.toString(), configurationElement.asElement());
				return;
			}
		}
		
		if (idMethod == null) {
			//TODO potential cycle
			pw.println(dtoType, " " + RESULT_NAME + " = createDtoInstance(null);");
		} else {
			
			boolean useIdConverter = false;

			NamedType dtoIdType = nameTypesUtils.toType(idMethod.getReturnType());
			DomainTypeElement domainIdTypeElement = null;
			
			if (idMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
				domainIdTypeElement = domainTypeElement.getId(entityResolver);
				NamedType dto = domainIdTypeElement.getDtoTypeElement();
				if (dto != null) {
					dtoIdType= dto;
				}
			}
							
			pw.println(dtoType, " " + RESULT_NAME + " = getDtoInstance(" + DOMAIN_NAME + ", " + DOMAIN_NAME + "." + MethodHelper.toGetter(MethodHelper.toField(idMethod)) + ");");
			pw.println("if (" + RESULT_NAME + " != null) {");
			pw.println("return " + RESULT_NAME + ";");
			pw.println("}");
			pw.println();
			
			String idName = "_id";
			
			pw.print(dtoIdType.getCanonicalName() + " " + idName + " = ");
			
			if (idMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
				if (domainIdTypeElement.getConfiguration() != null && domainIdTypeElement.getConfiguration().getConverter() != null) {
					converterProviderPrinter.printDomainConverterMethodName(domainIdTypeElement.getConfiguration().getConverter(), idMethod.getReturnType(), pw);
					pw.print(".convertToDto(");
					converterProviderPrinter.printDomainConverterMethodName(domainIdTypeElement.getConfiguration().getConverter(), idMethod.getReturnType(), pw);
					pw.print(".createDtoInstance(null), ");
					pw.print("(", castToDelegate(idMethod.getReturnType()), ")");
					useIdConverter = true;
				}
			}

			pw.print(DOMAIN_NAME + "." + MethodHelper.toGetter(MethodHelper.toField(idMethod)));

			if (useIdConverter) {
				pw.print(")");
			}
			pw.println(";");
			pw.println();

			pw.println(RESULT_NAME + " = createDtoInstance(" + DOMAIN_NAME + ", " + idName + ");");
		}

		pw.println("return convertToDto(" + RESULT_NAME + ", " + DOMAIN_NAME + ");");
		pw.println("}");
		pw.println();
		
		pw.println("public ", dtoType, " convertToDto(", dtoType, " " + RESULT_NAME + ", ", domainType, " " + DOMAIN_NAME + ") {");
		pw.println();
		pw.println("if (" + DOMAIN_NAME + "  == null) {");
		pw.println("return null;");
		pw.println("}");
		pw.println();


		DomainTypeElement domainsuperClass = configurationElement.getDomain().getSuperClass();
		
		if (domainsuperClass != null && domainsuperClass.getConfiguration().getConverter() != null) {
			converterProviderPrinter.printDomainConverterMethodName(domainsuperClass.getConfiguration().getConverter(), domainsuperClass.asType(), pw);
			pw.println(".convertToDto(" + RESULT_NAME + ", " + DOMAIN_NAME + ");");
			pw.println();
		}
	}
	
	@Override
	public void finish(ConfigurationTypeElement configurationTypeElement) {
		pw.println("return " + RESULT_NAME + ";");
		pw.println("}");
		pw.println();
	}
	
	protected void printIsInitializedMethod(PrintWriter pw, String instanceName) {
		pw.println("return true;");
	}
}