package sk.seges.sesam.pap.model.printer.equals;

import java.io.Serializable;
import java.util.Arrays;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.mutable.api.MutableArrayType;
import sk.seges.sesam.core.pap.model.mutable.api.MutableDeclaredType;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.utils.ProcessorUtils;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.Field;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.printer.AbstractDtoPrinter;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterTargetType;
import sk.seges.sesam.pap.model.resolver.ConverterConstructorParametersResolverProvider;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;

public class ConverterEqualsPrinter extends AbstractDtoPrinter implements TransferObjectElementPrinter {

	private static final String DTO_ID = "dtoId";

	private final ConverterProviderPrinter converterProviderPrinter;
	private final EntityResolver entityResolver;
	private final FormattedPrintWriter pw;

	public ConverterEqualsPrinter(ConverterProviderPrinter converterProviderPrinter, EntityResolver entityResolver, ConverterConstructorParametersResolverProvider parametersResolverProvider, 
			TransferObjectProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(parametersResolverProvider, processingEnv);
		this.entityResolver = entityResolver;
		this.converterProviderPrinter = converterProviderPrinter;
		this.pw = pw;
	}

	private String getDomainType(DomainDeclaredType instantiableType, boolean isCastRequired) {
		return isCastRequired ? "((" + instantiableType.getSimpleName() + ")" + DOMAIN_NAME + ")" : DOMAIN_NAME;
	}

	private void printDomainType(DomainDeclaredType instantiableType, boolean isCastRequired, FormattedPrintWriter pw) {
		if (isCastRequired) {
			pw.print("((", instantiableType.getSimpleName(), ")" + DOMAIN_NAME + ")");
		} else {
			pw.print(DOMAIN_NAME);
		}
	}
	
	@Override
	public void initialize(ConfigurationTypeElement configurationTypeElement, MutableDeclaredType outputName) {
		pw.println("public boolean equals(", configurationTypeElement.getDomain(), " " + DOMAIN_NAME + ", ",
											 configurationTypeElement.getDto(), " " + DTO_NAME + ") {");
		if (entityResolver.shouldHaveIdMethod(configurationTypeElement.getInstantiableDomain())) {

			DomainType domainId = configurationTypeElement.getDomain().getId(entityResolver);
			DomainType instantiableDomainId = configurationTypeElement.getInstantiableDomain().getId(entityResolver);
			
			if (domainId == null && instantiableDomainId != null) {
				
				MutableDeclaredType mutableSerializableType = processingEnv.getTypeUtils().toMutableType(Serializable.class);
				
				//In the data interface there is no ID defined (or defined using type variables), and is specified only id entity - so use object instead (or serializable, we'll see)
				domainId = processingEnv.getTransferObjectUtils().getDomainType(mutableSerializableType);
			}
			
			ExecutableElement domainIdMethod = configurationTypeElement.getInstantiableDomain().getIdMethod(entityResolver);
			
			if (domainIdMethod == null) {
				processingEnv.getMessager().printMessage(Kind.ERROR, configurationTypeElement.getInstantiableDomain().getCanonicalName() + " does not have an ID method implemented. Please implement ID method so equals method should be implemented.");
				return;
			}
			
			ExecutableElement getterMethod = configurationTypeElement.getDomain().getGetterMethod(MethodHelper.toField(domainIdMethod));
			
			boolean isCastRequired = getterMethod == null;

			if (!isCastRequired) {
				TypeMirror returnIdType = getterMethod.getReturnType();
				
				if (returnIdType.getKind().equals(TypeKind.TYPEVAR)) {
					TypeMirror erasedIdType = 
						ProcessorUtils.erasure(configurationTypeElement.getDomain().asElement(), (TypeVariable)returnIdType);
					
					if (erasedIdType != null) {
						returnIdType = erasedIdType;
					}
				}
				
				if (!processingEnv.getTypeUtils().isAssignable(returnIdType, 
						processingEnv.getElementUtils().getTypeElement(Serializable.class.getCanonicalName()).asType())) {
					
					processingEnv.getMessager().printMessage(Kind.ERROR, configurationTypeElement.getDomain().getCanonicalName() + 
							" has ID that does not implement serializable! ID method should implement serializable interface.");
				}
			}
			
			String methodNameSuffix = "." + domainIdMethod.getSimpleName().toString() + "()";
			String methodName = getDomainType(configurationTypeElement.getInstantiableDomain(), isCastRequired) + methodNameSuffix;
			
			if (domainId.getConverter() != null) {
				pw.print(domainId.getDto(), " " + DTO_ID + " = ");
				
				Field field = new Field(methodName, domainId);
				//converterProviderPrinter.printDomainEnsuredConverterMethodName(domainId, null, field, configurationTypeElement.getInstantiableDomain().getIdMethod(entityResolver), pw, false);
				converterProviderPrinter.printDomainGetConverterMethodName(domainId, field, configurationTypeElement.getInstantiableDomain().getIdMethod(entityResolver), pw, false);
				pw.print(".toDto(");
			} else {
				pw.print(domainId, " " + DTO_ID + " = ");
			}

			printDomainType(configurationTypeElement.getInstantiableDomain(), isCastRequired, pw);
			pw.print(methodNameSuffix);

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
		
		if (context.useConverter()) {
			String converterName = "converter" + MethodHelper.toMethod("", context.getDtoFieldName());
			pw.print(converterProviderPrinter.getDtoConverterType(context.getDomainMethodReturnType(), true), " " + converterName + " = ");
			converterProviderPrinter.printObtainConverterFromCache(ConverterTargetType.DOMAIN, context.getDomainMethodReturnType(), 
					new Field(TransferObjectElementPrinter.DOMAIN_NAME  + "." + context.getDomainFieldName(), null), context.getDomainMethod(), true);
			pw.println(";");
			pw.println("if (" + converterName + " != null && !" + converterName + ".equals(" + DOMAIN_NAME + "." + context.getDomainFieldName() + "," +
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