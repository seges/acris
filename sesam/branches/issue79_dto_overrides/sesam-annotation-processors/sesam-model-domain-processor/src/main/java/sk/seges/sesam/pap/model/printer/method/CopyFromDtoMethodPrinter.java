package sk.seges.sesam.pap.model.printer.method;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeMirror.MutableTypeKind;
import sk.seges.sesam.core.pap.model.mutable.api.MutableTypeVariable;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.model.api.domain.DomainDeclaredType;
import sk.seges.sesam.pap.model.model.api.domain.DomainType;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter.ConverterTargetType;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class CopyFromDtoMethodPrinter extends AbstractMethodPrinter implements CopyMethodPrinter {

	private EntityResolver entityResolver;
	
	private Set<String> instances = new HashSet<String>();
	
	public CopyFromDtoMethodPrinter(ConverterProviderPrinter converterProviderPrinter, EntityResolver entityResolver, ParametersResolver parametersResolver, RoundEnvironment roundEnv, TransferObjectProcessingEnvironment processingEnv) {
		super(converterProviderPrinter, parametersResolver, roundEnv, processingEnv);
		this.entityResolver = entityResolver;
	}

	@Override
	public void printCopyMethod(TransferObjectContext context, FormattedPrintWriter pw) {
		
		PathResolver pathResolver = new PathResolver(context.getDomainFieldPath());

		boolean nested = pathResolver.isNested();

		String currentPath = pathResolver.next();
		String fullPath = currentPath;
		String previousPath = TransferObjectElementPrinter.RESULT_NAME;
		
		DomainDeclaredType domainTypeElement = context.getConfigurationTypeElement().getDomain();
		
		if (nested && context.getConfigurationTypeElement().getDomain() != null) {
			
			DomainDeclaredType referenceDomainType = domainTypeElement;
			
			while (pathResolver.hasNext()) {

				DomainType domainReference = referenceDomainType.getDomainReference(currentPath);
				
				if (domainReference == null) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find getter method for the field " + currentPath + " in the " + domainTypeElement.toString(), context.getConfigurationTypeElement().asConfigurationElement());
					return;
				}

				if (!domainReference.getKind().isDeclared()) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Invalid mapping specified in the field " + currentPath + ". Current path (" + 
							fullPath + ") address getter type that is not class/interfaces." +
							"You probably mistyped this field in the configuration.", context.getConfigurationTypeElement().asConfigurationElement());

					return;
				}
				
				referenceDomainType = (DomainDeclaredType)domainReference;
				
				if (!instances.contains(fullPath)) {
					//TODO check if getId is null

					if (referenceDomainType.getKind().isDeclared()) {
						printCopyNested(pathResolver, fullPath, referenceDomainType, context.getDomainMethod(), pw);
						instances.add(fullPath);
					}
					
					if (instances.contains(currentPath)) {
						pw.println(previousPath + "." + MethodHelper.toSetter(currentPath) + "(" + currentPath + ");");
					}
				}

				previousPath = currentPath;
				currentPath = pathResolver.next();
				fullPath += MethodHelper.toMethod(currentPath);
			}

			if (context.getConfigurationTypeElement().getDomain() != null && domainTypeElement.getSetterMethod(context.getDomainFieldPath()) != null) {
				printCopy(pathResolver, context, pw);
			} else {
				ExecutableElement domainGetterMethod = context.getConfigurationTypeElement().getDomain().getGetterMethod(currentPath);
				
				VariableElement field = MethodHelper.getField(domainTypeElement.asConfigurationElement(), currentPath);
				
				if ((domainGetterMethod == null && field != null && !entityResolver.isIdField(field)) || !entityResolver.isIdMethod(domainGetterMethod)) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Setter is not available for the field " + currentPath + " in the class " + domainTypeElement.toString(), context.getConfigurationTypeElement().asConfigurationElement());
				}
			}
		} else {
			printCopy(pathResolver, context, pw);
		}
	}
	
	protected void printCopy(PathResolver pathResolver, TransferObjectContext context, FormattedPrintWriter pw) {
		if (context.getConverter() != null) {
			printCopyByConverter(context.getConverter(), context.getDomainMethod(), pathResolver, context.getDtoFieldName(), pw);
		} else if (context.isLocalConverter()) {
			String converterName = printLocalConverter(context, ConverterTargetType.DTO, pw);
			printCopyByLocalConverter(converterName, pathResolver, context.getDomainMethodReturnType(), context.getDtoFieldName(), pw);
		} else {
			printCopySimple(pathResolver, context.getConfigurationTypeElement().getDomain(), context.getConfigurationTypeElement(), context.getDtoFieldName(), pw);
		}
	}
	
	protected void printCopyNested(PathResolver domainPathResolver, String fullPath, DomainDeclaredType referenceDomainType, ExecutableElement method, FormattedPrintWriter pw) {
		if (referenceDomainType.getId(entityResolver) != null) {
			pw.print(referenceDomainType + " " + domainPathResolver.getCurrent() + " = ");
			converterProviderPrinter.printDtoConverterMethodName(referenceDomainType.getDto(), 
					TransferObjectElementPrinter.DTO_NAME, method, pw);
			pw.println(".getDomainInstance(" + TransferObjectElementPrinter.DTO_NAME + ", " + TransferObjectElementPrinter.DTO_NAME + "." + MethodHelper.toGetter(fullPath + MethodHelper.toMethod(MethodHelper.toField(referenceDomainType.getIdMethod(entityResolver)))) + ");");
		} else {
			pw.println(referenceDomainType + " " + domainPathResolver.getCurrent() + " = " + TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toGetter(domainPathResolver.getCurrent()) + ";");
			pw.println("if (" + TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toGetter(domainPathResolver.getCurrent()) + " == null) {");
			pw.print(domainPathResolver.getCurrent() + " = ");
			converterProviderPrinter.printDtoConverterMethodName(referenceDomainType.getDto(), 
					TransferObjectElementPrinter.DTO_NAME, method, pw);
			pw.println(".createDomainInstance(null);");
			pw.println("}");
		}
	}
	
	protected void printCopySimple(PathResolver domainPathResolver, DomainDeclaredType domainTypeElement, ConfigurationTypeElement configurationTypeElement, String dtoField, FormattedPrintWriter pw) {
		ExecutableElement domainGetterMethod = domainTypeElement.getGetterMethod(domainPathResolver.getCurrent());
		
		if (configurationTypeElement.getDomain().getSetterMethod(domainPathResolver.getPath()) != null) {
			pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(domainPathResolver.getPath()) + "(" + TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(dtoField));
		} else {
			
			VariableElement field = MethodHelper.getField(domainTypeElement.asConfigurationElement(), domainPathResolver.getCurrent());
			
			if ((domainGetterMethod == null && field != null && !entityResolver.isIdField(field)) || !entityResolver.isIdMethod(domainGetterMethod)) {
				processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Setter is not available for the field " + domainPathResolver.getCurrent() + " in the class " + domainTypeElement.toString(), configurationTypeElement.asConfigurationElement());
				return;
			}
		}
	
		pw.println(");");
	}

    protected void printCopyByConverter(ConverterTypeElement converter, ExecutableElement domainMethod, PathResolver domainPathResolver, String dtoField, FormattedPrintWriter pw) {
		String converterName = "converter" + MethodHelper.toMethod("", dtoField);
		pw.print(converter.getConverterBase(), " " + converterName + " = ");
		converterProviderPrinter.printDtoConverterMethodName(converter.getDto(), 
				TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(dtoField), domainMethod, pw);
		pw.println(";");
		pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(domainPathResolver.getPath()) + "(");
		pw.print("(", castToDelegate(converter.getDomain()), ")");
		pw.print(converterName + ".fromDto(");
		//TODO check for the nested
		//pw.print("(", castToDelegate(domainMethodReturnType), ")");
		pw.print(TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(dtoField));
		pw.println("));");
	}
	
	protected void printCopyByLocalConverter(String localConverterName, PathResolver domainPathResolver, DomainType domainMethodReturnType, String dtoField, FormattedPrintWriter pw) {
		pw.println("if (" + localConverterName + " != null) {");
		pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(domainPathResolver.getPath()) + "(" + 
				localConverterName + ".fromDto(" + TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(dtoField) + ")");
		pw.println(");");
		pw.println("} else {");
		pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(domainPathResolver.getPath()));
		if (domainMethodReturnType.getKind().equals(MutableTypeKind.TYPEVAR)) {
			pw.print("((" + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "_" + ((MutableTypeVariable)domainMethodReturnType).getVariable() + ")");
		} else {
			pw.print("((" + domainMethodReturnType + ")");
		}
		pw.print(TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(dtoField));
		pw.println(");");
		pw.println("}");
	}	
}