package sk.seges.sesam.pap.model.printer.method;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeVariable;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.utils.MethodHelper;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.TransferObjectContext;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.model.TransferObjectProcessingEnvironment;
import sk.seges.sesam.pap.model.printer.api.TransferObjectElementPrinter;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
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
		
		DomainTypeElement domainTypeElement = context.getConfigurationTypeElement().getDomain();
		
		if (nested && context.getConfigurationTypeElement().getDomain() != null) {
			
			DomainTypeElement referenceDomainType = domainTypeElement;
			
			while (pathResolver.hasNext()) {

				referenceDomainType = referenceDomainType.getDomainReference(currentPath);
				
				if (referenceDomainType == null) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find getter method for the field " + currentPath + " in the " + domainTypeElement.toString(), context.getConfigurationTypeElement().asElement());
					return;
				}

				if (!instances.contains(fullPath)) {
					//TODO check if getId is null

					if (referenceDomainType.getKind().isDeclared()) {

						if (referenceDomainType.getId(entityResolver) != null) {
							pw.print(referenceDomainType + " " + currentPath + " = ");
							converterProviderPrinter.printDomainConverterMethodName(referenceDomainType.getConfiguration().getConverter(), referenceDomainType.asType(), pw);
							pw.println(".getDomainInstance(" + TransferObjectElementPrinter.DTO_NAME + "." + MethodHelper.toGetter(fullPath + MethodHelper.toMethod(MethodHelper.toField(referenceDomainType.getIdMethod(entityResolver)))) + ");");
							instances.add(fullPath);
						} else {
							pw.println(referenceDomainType + " " + currentPath + " = " + TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toGetter(currentPath) + ";");
							pw.println("if (" + TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toGetter(currentPath) + " == null) {");
							pw.print(currentPath + " = ");
							converterProviderPrinter.printDomainConverterMethodName(referenceDomainType.getConfiguration().getConverter(), referenceDomainType.asType(), pw);
							pw.println(".createDomainInstance(null);");
							instances.add(fullPath);
							pw.println("}");
							
						}
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
				if (context.getConverter() != null) {
					String converterName = "converter" + MethodHelper.toMethod("", context.getFieldName());
					pw.print(context.getConverter(), " " + converterName + " = ");
					converterProviderPrinter.printDomainConverterMethodName(context.getConverter(), context.getDomainMethodReturnType(), pw);
					pw.println(";");
					pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(context.getDomainFieldPath()) + "("+ converterName + ".fromDto(");
					pw.print("(", castToDelegate(context.getDomainMethodReturnType()), ")");
					pw.print(TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(context.getFieldName()));
				} else {
					pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(context.getDomainFieldPath()) + "(" + TransferObjectElementPrinter.DTO_NAME + "." + MethodHelper.toGetter(context.getFieldName()));
				}
				if (context.getConverter() != null) {
					pw.print(")");
				}
				pw.println(");");
			} else {
				ExecutableElement domainGetterMethod = context.getConfigurationTypeElement().getDomain().getGetterMethod(currentPath);
				
				VariableElement field = MethodHelper.getField(domainTypeElement.asElement(), currentPath);
				
				if ((domainGetterMethod == null && field != null && !entityResolver.isIdField(field)) || !entityResolver.isIdMethod(domainGetterMethod)) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Setter is not available for the field " + currentPath + " in the class " + domainTypeElement.toString(), context.getConfigurationTypeElement().asElement());
				}
			}
		} else {
			boolean generated = true;
			if (context.getConverter() != null) {
				String converterName = "converter" + MethodHelper.toMethod("", context.getFieldName());
				pw.print(context.getConverter(), " " + converterName + " = ");
				converterProviderPrinter.printDomainConverterMethodName(context.getConverter(), context.getDomainMethodReturnType(), pw);
				pw.println(";");
				pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(context.getDomainFieldPath()) + "(");
				pw.print("(", castToDelegate(context.getDomainMethodReturnType()), ")");
				pw.print(converterName + ".fromDto(" + TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(context.getFieldName()));
			} else if (context.getLocalConverterName() != null) {
				pw.println("if (" + context.getLocalConverterName() + " != null) {");
				pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(context.getDomainFieldPath()) + "(" + 
						context.getLocalConverterName() + ".fromDto(" + TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(context.getFieldName()) + ")");
			} else {
				ExecutableElement domainGetterMethod = domainTypeElement.getGetterMethod(currentPath);
				
				if (context.getConfigurationTypeElement().getDomain().getSetterMethod(context.getDomainFieldPath()) != null) {
					pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(context.getDomainFieldPath()) + "(" + TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(context.getFieldName()));
				} else {
					
					VariableElement field = MethodHelper.getField(domainTypeElement.asElement(), currentPath);
					
					if ((domainGetterMethod == null && field != null && !entityResolver.isIdField(field)) || !entityResolver.isIdMethod(domainGetterMethod)) {
						processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Setter is not available for the field " + currentPath + " in the class " + domainTypeElement.toString(), context.getConfigurationTypeElement().asElement());
						generated = false;
					}
				}
			}
			
			if (generated) {
				if (context.getConverter() != null) {
					pw.print(")");
				}
				pw.println(");");
			}

			if (context.getLocalConverterName() != null) {
				pw.println("} else {");
				pw.print(TransferObjectElementPrinter.RESULT_NAME + "." + MethodHelper.toSetter(context.getDomainFieldPath()));
				if (context.getDomainMethodReturnType().getKind().equals(TypeKind.TYPEVAR)) {
					pw.print("((" + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "_" + ((TypeVariable)context.getDomainMethodReturnType()).asElement().getSimpleName().toString() + ")");
				} else {
					pw.print("((" + context.getDomainMethodReturnType() + ")");
				}
				pw.print(TransferObjectElementPrinter.DTO_NAME  + "." + MethodHelper.toGetter(context.getFieldName()));
				pw.println(");");
				pw.println("}");
			}
		}
	}
}