package sk.seges.sesam.pap.model.printer.method;

import java.io.Serializable;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.api.element.MutableExecutableElement;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.Field;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoDeclaredType;
import sk.seges.sesam.pap.model.model.api.dto.DtoType;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterTargetType;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public class CopyFromDtoPrinter extends AbstractMethodPrinter implements TransferObjectElementPrinter {

	protected final FormattedPrintWriter pw;
	protected final Set<String> nestedInstances;
	
	public CopyFromDtoPrinter(Set<String> nestedInstances, ConverterProviderPrinter converterProviderPrinter, EntityResolver entityResolver, 
			ConverterConstructorParametersResolverProvider parametersResolverProvider, RoundEnvironment roundEnv, TransferObjectProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(converterProviderPrinter, parametersResolverProvider, entityResolver, roundEnv, processingEnv);
		this.nestedInstances = nestedInstances;
		this.pw = pw;
	}
	
	@Override
	public void print(TransferObjectContext context) {
		copy(context, pw, new CopyFromDtoMethodPrinter(nestedInstances, converterProviderPrinter, entityResolver, parametersResolverProvider, roundEnv, processingEnv));
	}

	@Override
	public void initialize(ConfigurationTypeElement configurationElement, MutableDeclaredType outputName) {
		
		DtoDeclaredType dtoType = configurationElement.getDto();
		DomainDeclaredType domainType = configurationElement.getDomain();
		
		ExecutableElement domainIdMethod = null;
		
		if (domainType.getKind().isDeclared()) {
			domainIdMethod = configurationElement.getInstantiableDomain().getIdMethod(entityResolver);
			
			if (domainIdMethod == null && entityResolver.shouldHaveIdMethod(configurationElement.getInstantiableDomain())) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method for " + configurationElement.toString(), configurationElement.asConfigurationElement());
				return;
			}
		}
		
		pw.println("public ", domainType, " createDomainInstance(", Serializable.class, " id) {");
		
		printDomainInstancer(pw, configurationElement.getInstantiableDomain());
		pw.println("}");
		pw.println();

		pw.println("public ", domainType, " fromDto(", dtoType, " " + DTO_NAME + ") {");
		pw.println();
		pw.println("if (" + DTO_NAME + " == null) {");
		pw.println("return null;");
		pw.println("}");
		pw.println();
		
		MutableExecutableElement dtoIdMethod = configurationElement.getDto().getIdMethod(entityResolver);
		
		if (dtoIdMethod == null && entityResolver.shouldHaveIdMethod(configurationElement.getInstantiableDomain())) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method for DTO class " + dtoType.getCanonicalName(), configurationElement.asConfigurationElement());
			return;
		}
		
		if (dtoIdMethod == null) {
			//TODO potential cycle
			pw.println(domainType, " " + RESULT_NAME + " = createDomainInstance(null);");
		} else {

			boolean useIdConverter = false;
			
			pw.println(domainType, " " + RESULT_NAME + " = getDomainInstance(" + DTO_NAME + ", " + DTO_NAME + "." + MethodHelper.toGetter(MethodHelper.toField(dtoIdMethod)) + ");");
			pw.println("if (" + RESULT_NAME + " != null) {");
			pw.println("return " + RESULT_NAME + ";");
			pw.println("}");

			DtoType dtoIdType = processingEnv.getTransferObjectUtils().getDtoType(dtoIdMethod.asType().getReturnType());

			String idName = "_id";
			
			pw.print(dtoIdType.getDomain(), " " + idName + " = ");
			
			if (domainIdMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
				ConverterTypeElement idConverter = dtoIdType.getConverter();

				if (idConverter != null) {
					Field field = new Field(DTO_NAME + "." + MethodHelper.toGetter(MethodHelper.toField(dtoIdMethod)), dtoIdType);
					converterProviderPrinter.printObtainConverterFromCache(pw, ConverterTargetType.DTO, dtoIdType.getDomainDefinitionConfiguration().getInstantiableDomain(), field, domainIdMethod, true);
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

			pw.println(RESULT_NAME + " = createDomainInstance(" + idName + ");");
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

		if (dtoIdMethod != null) {
			pw.println(domainType, " domainFromCache = getDomainFromCache(" + DTO_NAME + ", " + DTO_NAME + "." + MethodHelper.toGetter(MethodHelper.toField(dtoIdMethod)) + ");");
			pw.println();
			pw.println("if (domainFromCache != null) {");
			pw.println("return domainFromCache;");
			pw.println("}");
			pw.println();
		}
		
		DomainDeclaredType domainSuperClass = configurationElement.getDomain().getSuperClass();
		
		if (domainSuperClass != null && domainSuperClass.getConverter() != null) {
			   domainSuperClass = domainSuperClass.getDomainDefinitionConfiguration().getInstantiableDomain();
		}
		
		if (domainSuperClass != null && domainSuperClass.getConverter() != null && domainSuperClass.getKind().equals(MutableTypeKind.CLASS)) {
			MutableDeclaredType fieldType = processingEnv.getTypeUtils().getDeclaredType(processingEnv.getTypeUtils().toMutableType(Class.class), 
					new MutableDeclaredType[] { domainSuperClass.getDto() });
			//TODO: change canonical name to simple name and add import
			Field field = new Field(domainSuperClass.getDto().getCanonicalName() + ".class", fieldType);
			
			converterProviderPrinter.printObtainConverterFromCache(pw, ConverterTargetType.DTO, domainSuperClass, field, null, false);

			pw.println(".convertFromDto(" + RESULT_NAME + ", " + DTO_NAME + ");");
			pw.println();
		}

		if (dtoIdMethod != null) {
			pw.println("putDomainIntoCache(" + DTO_NAME + ", " + RESULT_NAME + "," + RESULT_NAME + "." + MethodHelper.toGetter(MethodHelper.toField(domainIdMethod)) + ");");
			pw.println();
		}
	}
	
	@Override
	public void finish(ConfigurationTypeElement configuratioTypeElement) {
		pw.println("return " + RESULT_NAME + ";");
		pw.println("}");
		pw.println();
	}

	protected void printDomainInstancer(FormattedPrintWriter pw, DomainDeclaredType domainTypeElement) {
		if (!entityResolver.shouldHaveIdMethod(domainTypeElement)) {
			pw.println(" return new ", domainTypeElement, "();");
		} else {
			pw.println(domainTypeElement, " " + RESULT_NAME, " = new ", domainTypeElement, "();");
			
			String setterMethod = MethodHelper.toSetter(domainTypeElement.getIdMethod(entityResolver));
			
			if (ProcessorUtils.hasMethod(setterMethod, domainTypeElement.asElement())) {
				pw.println(RESULT_NAME + "." + MethodHelper.toSetter(domainTypeElement.getIdMethod(entityResolver)) + "((", domainTypeElement.getId(entityResolver), ")" + "id);");
			} else {
				pw.println("if (id != null) {");
				pw.println("throw new ", RuntimeException.class, "(\"Unable to define ID for imutable entity. Please define " + setterMethod + " method for ", domainTypeElement, "!\");");
				pw.println("}");
			}
			pw.println("return " + RESULT_NAME + ";");
		}
	}
}