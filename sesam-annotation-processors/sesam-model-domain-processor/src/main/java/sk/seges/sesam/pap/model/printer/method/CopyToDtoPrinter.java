package sk.seges.sesam.pap.model.printer.method;

import java.io.PrintWriter;
import java.io.Serializable;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.ElementHolderTypeConverter;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class CopyToDtoPrinter extends AbstractMethodPrinter implements TransferObjectElementPrinter {

	protected final FormattedPrintWriter pw;

	protected ElementHolderTypeConverter elementHolderTypeConverter;
	protected EntityResolver entityResolver;
	
	public CopyToDtoPrinter(ConverterProviderPrinter converterProviderPrinter, ElementHolderTypeConverter elementHolderTypeConverter, EntityResolver entityResolver, ParametersResolver parametersResolver, 
			RoundEnvironment roundEnv, TransferObjectProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(converterProviderPrinter, parametersResolver, roundEnv, processingEnv);
		this.pw = pw;
		this.elementHolderTypeConverter = elementHolderTypeConverter;
		this.entityResolver = entityResolver;
	}
	
	@Override
	public void print(TransferObjectContext context) {
		copy(context, pw, new CopyToDtoMethodPrinter(converterProviderPrinter, elementHolderTypeConverter, parametersResolver, roundEnv, processingEnv));
	}

	@Override
	public void initialize(ConfigurationTypeElement configurationElement, MutableDeclaredType outputName) {
		
		DtoDeclaredType dtoType = configurationElement.getDto();
		DomainDeclaredType domainType = configurationElement.getDomain();
		
		DomainDeclaredType domainTypeElement = domainType;

//		String instanceName = "instance";
		
//		pw.println("protected boolean isInitialized(", Object.class, " " + instanceName + ") {");
//		printIsInitializedMethod(pw, instanceName);
//		pw.println("}");
//		pw.println();
		
		pw.println("public ", dtoType, " createDtoInstance(", Serializable.class, " id) {");
		printDtoInstancer(pw, entityResolver, dtoType);
		pw.println("}");
		pw.println();
							
		pw.println("public ", dtoType, " toDto(", domainType, " " + DOMAIN_NAME + ") {");
		pw.println();
		pw.println("if (" + DOMAIN_NAME + "  == null) {");
		pw.println("return null;");
		pw.println("}");
		pw.println();

		ExecutableElement idMethod = null;
		
		if (domainType.getKind().isDeclared()) {
			idMethod = domainTypeElement.getIdMethod(entityResolver);
			
			if (idMethod == null && entityResolver.shouldHaveIdMethod(domainTypeElement)) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method for " + configurationElement.toString(), configurationElement.asConfigurationElement());
				return;
			}
		}
		
		if (idMethod == null) {
			//TODO potential cycle
			pw.println(dtoType, " " + RESULT_NAME + " = createDtoInstance(null);");
		} else {

			boolean useIdConverter = false;

			MutableTypeMirror dtoIdType = processingEnv.getTypeUtils().toMutableType(idMethod.getReturnType());
			DomainType domainIdTypeElement = null;
			
			if (idMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
				domainIdTypeElement = domainTypeElement.getId(entityResolver);
				DtoType dto = domainIdTypeElement.getDto();
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
			
			pw.print(dtoIdType, " " + idName + " = ");
			
			if (idMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
				if (domainIdTypeElement.getConfiguration() != null && domainIdTypeElement.getConfiguration().getConverter() != null) {
					converterProviderPrinter.printDomainConverterMethodName(domainIdTypeElement.getConfiguration().getConverter(), processingEnv.getTypeUtils().toMutableType(idMethod.getReturnType()), idMethod, pw);
					pw.print(".convertToDto(");
					converterProviderPrinter.printDomainConverterMethodName(domainIdTypeElement.getConfiguration().getConverter(), processingEnv.getTypeUtils().toMutableType(idMethod.getReturnType()), idMethod, pw);
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

			pw.println(RESULT_NAME + " = createDtoInstance(" + idName + ");");
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

		if (idMethod != null) {
			pw.println(dtoType, " dtoFromCache = getDtoFromCache(" + DOMAIN_NAME + ", " + DOMAIN_NAME + "." + MethodHelper.toGetter(MethodHelper.toField(idMethod)) + ");");
			pw.println();
			pw.println("if (dtoFromCache != null) {");
			pw.println("return dtoFromCache;");
			pw.println("}");
			pw.println();
			pw.println("putDtoIntoCache(" + DOMAIN_NAME + ", " + RESULT_NAME + "," + RESULT_NAME + "." + MethodHelper.toGetter(MethodHelper.toField(idMethod)) + ");");
			pw.println();
		}
		
		DomainDeclaredType domainsuperClass = configurationElement.getDomain().getSuperClass();
		
		if (domainsuperClass != null && domainsuperClass.getConfiguration().getConverter() != null) {
			converterProviderPrinter.printDomainConverterMethodName(domainsuperClass.getConfiguration().getConverter(), domainsuperClass, null, pw);
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
	
	protected void printIsInitializedMethod(PrintWriter pw, ExecutableElement domainMethod, PathResolver domainPathResolver) {
//	protected void printIsInitializedMethod(PrintWriter pw, String instanceName) {
		pw.println("return true;");
	}
}