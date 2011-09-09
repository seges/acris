package sk.seges.sesam.pap.model.printer.method;

import java.io.PrintWriter;
import java.io.Serializable;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.api.ImmutableType;
import sk.seges.sesam.core.pap.model.api.NamedType;
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

	private static final String RESULT_NAME = "_result";
	private static final String DTO_NAME = "_dto";

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
				
		TypeMirror domainTypeMirror = configurationElement.getDomainTypeElement().asType();

		ExecutableElement idMethod = null;
		
		if (domainTypeMirror.getKind().equals(TypeKind.DECLARED)) {
			idMethod = toHelper.getIdMethod((DeclaredType)domainTypeMirror, entityResolver);
			
			if (idMethod == null) {
				idMethod = toHelper.getIdMethod((DeclaredType)configurationElement.asElement().asType(), entityResolver);
			}
			
			if (idMethod == null && entityResolver.shouldHaveIdMethod(configurationElement, domainTypeMirror)) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method for " + configurationElement.toString(), configurationElement.asElement());
				return;
			}
		}
		
		pw.println("public " + domainType.toString(ClassSerializer.SIMPLE, true) + " createDomainInstance(" + Serializable.class.getSimpleName() + " id) {");
		
		printDomainInstancer(pw, domainType);
		pw.println("}");
		pw.println();

		if (typeParametersSupport.hasTypeParameters(domainType)) {
			pw.println("@SuppressWarnings(\"rawtypes\")");
		}
		pw.println("protected " + Class.class.getSimpleName() + "<" + dtoType.getSimpleName() + "> getDtoClass() {");
		pw.println("return " + dtoType.getSimpleName() + ".class;");
		pw.println("}");
		pw.println();

		pw.println("public " + domainType.toString(ClassSerializer.SIMPLE, true) + " fromDto(" + dtoType.toString(ClassSerializer.SIMPLE, true) + " " + DTO_NAME + ") {");
		pw.println();
		pw.println("if (" + DTO_NAME + " == null) {");
		pw.println("return null;");
		pw.println("}");
		pw.println();
		
		ExecutableElement dtoIdMethod = toHelper.getDtoIdMethod(configurationElement, entityResolver);
		
		if (dtoIdMethod == null && entityResolver.shouldHaveIdMethod(configurationElement, domainTypeMirror)) {
			processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find id method for DTO class " + dtoType.getCanonicalName(), configurationElement.asElement());
			return;
		}
		
		if (dtoIdMethod == null) {
			//TODO potential cycle
			pw.println(domainType.toString(ClassSerializer.SIMPLE, true) + " " + RESULT_NAME + " = createDomainInstance(null);");
		} else {
			
			boolean useIdConverter = false;

			NamedType dtoIdType = nameTypesUtils.toType(idMethod.getReturnType());
			DomainTypeElement domainIdType = null;
			
			if (idMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
				domainIdType = new DomainTypeElement(idMethod.getReturnType(), processingEnv, roundEnv);
				if (domainIdType.getConfigurationTypeElement() != null) {
					DtoTypeElement dto = domainIdType.getConfigurationTypeElement().getDtoTypeElement();
					if (dto != null) {
						dtoIdType= dto;
					}
				}
			}

			pw.println(domainType.toString(ClassSerializer.SIMPLE, true) + " " + RESULT_NAME + " = getDomainInstance(" + DTO_NAME + ", " + DTO_NAME + "." + methodHelper.toGetter(methodHelper.toField(dtoIdMethod)) + ");");
			pw.println("if (" + RESULT_NAME + " != null) {");
			pw.println("return " + RESULT_NAME + ";");
			pw.println("}");

			String idName = "_id";
			
			if (domainIdType != null) {
				pw.print(domainIdType.toString() + " " + idName + " = ");
			} else {
				//Types are the same
				pw.print(dtoIdType.getCanonicalName() + " " + idName + " = ");
			}
			
			if (idMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
				ConfigurationTypeElement idConfigurationElement = domainIdType.getConfigurationTypeElement();
					//toHelper.getConfigurationElement(domainIdType, roundEnv);
				if (idConfigurationElement != null && idConfigurationElement.getConverterTypeElement() != null) {
					converterProviderPrinter.printDomainConverterMethodName(idConfigurationElement.getConverterTypeElement(), domainIdType.asType(), pw);
					pw.print(".fromDto(");
					useIdConverter = true;
				}
			}

			pw.print(DTO_NAME + "." + methodHelper.toGetter(methodHelper.toField(dtoIdMethod)));

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
		pw.println("public " + domainType.toString(ClassSerializer.SIMPLE, true) + " convertFromDto(" + domainType.toString(ClassSerializer.SIMPLE, true) + " " + RESULT_NAME + ", " + dtoType.toString(ClassSerializer.SIMPLE, true) + " " + DTO_NAME + ") {");
		pw.println();
		pw.println("if (" + DTO_NAME + "  == null) {");
		pw.println("return null;");
		pw.println("}");
		pw.println();

		DomainTypeElement domainsuperClass = configurationElement.getDomainTypeElement().getSuperClass();
		
		if (domainsuperClass != null && domainsuperClass.getConfigurationTypeElement().getConverterTypeElement() != null) {
			converterProviderPrinter.printDomainConverterMethodName(domainsuperClass.getConfigurationTypeElement().getConverterTypeElement(), domainsuperClass.asType(), pw);
			pw.println(".convertFromDto(" + RESULT_NAME + ", " + DTO_NAME + ");");
			pw.println();
		}
		
//		NamedType dtoSuperclass = toHelper.getDtoSuperclass(configurationElement.asElement());
//		
//		if (dtoSuperclass != null) {
//			TypeElement superClassElement = (TypeElement)((DeclaredType)domainObjectClass.getSuperclass()).asElement();
//			
//			Element superClassConfigurationElement = toHelper.getConfigurationElement(superClassElement, roundEnv);
//			
//			NamedType domainConverter = getDomainConverter(superClassElement);
//			
//			if (domainConverter != null && superClassConfigurationElement != null) {
//				printConverterInstance(pw, domainConverter, (TypeElement)superClassConfigurationElement);
//				pw.println(".convertFromDto(" + RESULT_NAME + ", " + DTO_NAME + ");");
//				pw.println();
//			}
//		}
	}
	
	@Override
	public void finish(ConfigurationTypeElement configuratioTypeElement) {
		pw.println("return " + RESULT_NAME + ";");
		pw.println("}");
		pw.println();
	}

	protected void printDomainInstancer(PrintWriter pw, NamedType type) {
		pw.println("return new " + type.toString(ClassSerializer.SIMPLE, true) + "();");
	}
}