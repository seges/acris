package sk.seges.sesam.pap.model.printer.method;

import java.io.PrintWriter;
import java.io.Serializable;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.model.api.ElementHolderTypeConverter;
import sk.seges.sesam.pap.model.printer.api.ElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.resolver.api.IdentityResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class CopyToDtoPrinter extends AbstractMethodPrinter implements ElementPrinter {

	private static final String DOMAIN_NAME = "_domain";
	private static final String RESULT_NAME = "_result";

	private final FormattedPrintWriter pw;

	private ElementHolderTypeConverter elementHolderTypeConverter;
	private EntityResolver entityResolver;
	private IdentityResolver identityResolver;
	
	public CopyToDtoPrinter(ConverterProviderPrinter converterProviderPrinter, ElementHolderTypeConverter elementHolderTypeConverter, IdentityResolver identityResolver, EntityResolver entityResolver, ParametersResolver parametersResolver, 
			RoundEnvironment roundEnv, ProcessingEnvironment processingEnv, FormattedPrintWriter pw) {
		super(converterProviderPrinter, parametersResolver, roundEnv, processingEnv);
		this.pw = pw;
		this.elementHolderTypeConverter = elementHolderTypeConverter;
		this.entityResolver = entityResolver;
		this.identityResolver = identityResolver;
	}
	
	@Override
	public void print(ProcessorContext context) {
		copy(context, pw, new CopyToDtoMethodPrinter(converterProviderPrinter, elementHolderTypeConverter, entityResolver, parametersResolver, roundEnv, processingEnv));
	}

	@Override
	public void initialize(ConfigurationTypeElement configurationElement) {
		
		ImmutableType dtoType = getDtoType(configurationElement);
		ImmutableType domainType = getDomainType(configurationElement);
		
		DomainTypeElement domainTypeElement = configurationElement.getDomainTypeElement();

		String instanceName = "instance";
		
		pw.println("protected boolean isInitialized(" + Object.class.getSimpleName() + " " + instanceName + ") {");
		printIsInitializedMethod(pw, instanceName);
		pw.println("}");
		pw.println();
		
		pw.println("public " + dtoType.toString(ClassSerializer.SIMPLE, true) + " createDtoInstance(" + Serializable.class.getSimpleName() + " id) {");
		printDtoInstancer(pw, dtoType);
		pw.println("}");
		pw.println();
		
		if (typeParametersSupport.hasTypeParameters(domainType)) {
			pw.println("@SuppressWarnings(\"rawtypes\")");
		}
		pw.println("protected " + Class.class.getSimpleName() + "<" + domainTypeElement.getSimpleName() + "> getDomainClass() {");
		pw.println("return " + domainTypeElement.getSimpleName() + ".class;");
		pw.println("}");
		pw.println();
					
		pw.println("public " + dtoType.toString(ClassSerializer.SIMPLE, true) + " toDto(" + domainType.toString(ClassSerializer.SIMPLE, true) + " " + DOMAIN_NAME + ") {");
		pw.println();
		pw.println("if (" + DOMAIN_NAME + "  == null) {");
		pw.println("return null;");
		pw.println("}");
		pw.println();

		ExecutableElement idMethod = null;
		
		if (domainType.asType().getKind().equals(TypeKind.DECLARED)) {
			idMethod = toHelper.getIdMethod((DeclaredType)domainTypeElement.asType());
			
			if (idMethod == null) {
				idMethod = toHelper.getIdMethod((DeclaredType) configurationElement.asElement().asType());
			}
			
			if (idMethod == null && identityResolver.shouldHaveIdMethod(configurationElement, domainTypeElement.asType())) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method for " + configurationElement.toString(), configurationElement.asElement());
				return;
			}
		}
		
		if (idMethod == null) {
			//TODO potential cycle
			pw.println(dtoType.toString(ClassSerializer.SIMPLE, true) + " " + RESULT_NAME + " = createDtoInstance(null);");
		} else {
			
			boolean useIdConverter = false;

			NamedType dtoIdType = nameTypesUtils.toType(idMethod.getReturnType());
			DomainTypeElement domainIdTypeElement = null;
			
			if (idMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
				domainIdTypeElement = new DomainTypeElement(idMethod.getReturnType(), processingEnv, roundEnv);
				NamedType dto = domainIdTypeElement.getDtoTypeElement();
				if (dto != null) {
					dtoIdType= dto;
				}
			}
							
			pw.println(dtoType.toString(ClassSerializer.SIMPLE, true) + " " + RESULT_NAME + " = getDtoInstance(" + DOMAIN_NAME + ", " + DOMAIN_NAME + "." + methodHelper.toGetter(methodHelper.toField(idMethod)) + ");");
			pw.println("if (" + RESULT_NAME + " != null) {");
			pw.println("return " + RESULT_NAME + ";");
			pw.println("}");
			pw.println();
			
			String idName = "_id";
			
			pw.print(dtoIdType.getCanonicalName() + " " + idName + " = ");
			
			if (idMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
				//Element idConfigurationElement = toHelper.getConfigurationElement(domainIdType, roundEnv);
				//getDomainConverter(domainIdType);
				if (domainIdTypeElement.getConfigurationTypeElement() != null && domainIdTypeElement.getConfigurationTypeElement().getConverterTypeElement() != null) {
					converterProviderPrinter.printDomainConverterMethodName(domainIdTypeElement.getConfigurationTypeElement().getConverterTypeElement(), idMethod.getReturnType(), pw);
					pw.print(".toDto(");
					pw.print("(" + castToDelegate(idMethod.getReturnType()).toString(ClassSerializer.CANONICAL, true) + ")");
					useIdConverter = true;
				}
			}

			pw.print(DOMAIN_NAME + "." + methodHelper.toGetter(methodHelper.toField(idMethod)));

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
		
		pw.println("public " + dtoType.toString(ClassSerializer.SIMPLE, true) + " convertToDto(" + dtoType.toString(ClassSerializer.SIMPLE, true) + " " + RESULT_NAME + ", " + 
				domainType.toString(ClassSerializer.SIMPLE, true) + " " + DOMAIN_NAME + ") {");
		pw.println();
		pw.println("if (" + DOMAIN_NAME + "  == null) {");
		pw.println("return null;");
		pw.println("}");
		pw.println();


		DomainTypeElement domainsuperClass = configurationElement.getDomainTypeElement().getSuperClass();
		
		if (domainsuperClass != null && domainsuperClass.getConfigurationTypeElement().getConverterTypeElement() != null) {
//			DtoTypeElement dtoSuperTypeElement = domainsuperClass.getConfigurationTypeElement().getDtoTypeElement();
//		}
		
//		NamedType dtoSuperclass = toHelper.getDtoSuperclass(configurationElement.asElement());
//		if (dtoSuperclass != null) {
//			TypeElement superClassElement = (TypeElement)((DeclaredType)domainTypeElement.asElement().getSuperclass()).asElement();
//			
//			Element superClassConfigurationElement = toHelper.getConfigurationElement(superClassElement, roundEnv);
//			
//			NamedType domainConverter = getDomainConverter(superClassElement);

//			if (dtoSuperTypeElement != null) {
//			if (domainConverter != null && superClassConfigurationElement != null) {
				converterProviderPrinter.printDomainConverterMethodName(domainsuperClass.getConfigurationTypeElement().getConverterTypeElement(), domainsuperClass.asType(), pw);
				pw.println(".convertToDto(" + RESULT_NAME + ", " + DOMAIN_NAME + ");");
				pw.println();
//			}
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