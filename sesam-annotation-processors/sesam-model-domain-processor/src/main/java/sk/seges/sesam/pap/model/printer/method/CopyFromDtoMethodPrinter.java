package sk.seges.sesam.pap.model.printer.method;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeVariable;
import javax.tools.Diagnostic.Kind;

import sk.seges.sesam.core.pap.builder.api.NameTypes.ClassSerializer;
import sk.seges.sesam.core.pap.model.PathResolver;
import sk.seges.sesam.core.pap.writer.FormattedPrintWriter;
import sk.seges.sesam.pap.model.context.api.ProcessorContext;
import sk.seges.sesam.pap.model.model.ConfigurationTypeElement;
import sk.seges.sesam.pap.model.model.ConverterTypeElement;
import sk.seges.sesam.pap.model.model.DomainTypeElement;
import sk.seges.sesam.pap.model.printer.converter.ConverterProviderPrinter;
import sk.seges.sesam.pap.model.resolver.api.EntityResolver;
import sk.seges.sesam.pap.model.resolver.api.ParametersResolver;

public class CopyFromDtoMethodPrinter extends AbstractMethodPrinter implements CopyMethodPrinter {

	private static final String RESULT_NAME = "_result";
	private static final String DTO_NAME = "_dto";

	private EntityResolver entityResolver;
	
	private Set<String> instances = new HashSet<String>();
	
	public CopyFromDtoMethodPrinter(ConverterProviderPrinter converterProviderPrinter, EntityResolver entityResolver, ParametersResolver parametersResolver, RoundEnvironment roundEnv, ProcessingEnvironment processingEnv) {
		super(converterProviderPrinter, parametersResolver, roundEnv, processingEnv);
		this.entityResolver = entityResolver;
	}

	@Override
	public void printCopyMethod(ProcessorContext context, FormattedPrintWriter pw) {
		
		PathResolver pathResolver = new PathResolver(context.getDomainFieldPath());

		boolean nested = pathResolver.isNested();

		String currentPath = pathResolver.next();
		String fullPath = currentPath;
		String previousPath = RESULT_NAME;
					
		Element element = context.getConfigurationTypeElement().asElement();
		TypeElement domainTypeElement = null;
		
		if (context.getConfigurationTypeElement().getDomainTypeElement() != null) {
			domainTypeElement = context.getConfigurationTypeElement().getDomainTypeElement().asElement();
		}

		if (nested) {
			while (pathResolver.hasNext()) {

				ExecutableElement domainGetterMethod = toHelper.getDomainGetterMethod(element, currentPath);
				
				if (domainGetterMethod == null) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find getter method for the field " + currentPath + " in the " + element.toString(), context.getConfigurationTypeElement().asElement());
					return;
				}

				if (!instances.contains(fullPath)) {
					//TODO check if getId is null

					if (domainGetterMethod.getReturnType().getKind().equals(TypeKind.DECLARED)) {
						Element referenceElement = ((DeclaredType)domainGetterMethod.getReturnType()).asElement();

						ConfigurationTypeElement configurationElement = new DomainTypeElement(domainGetterMethod.getReturnType(), processingEnv, roundEnv).getConfigurationTypeElement();

						if (configurationElement == null) {
							processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Unable to find conversion configuration for type " + referenceElement.toString(), context.getConfigurationTypeElement().asElement());
							return;
						}
						
						if (toHelper.getIdMethod((DeclaredType)domainGetterMethod.getReturnType(), entityResolver) != null) {
							pw.print(nameTypesUtils.toType(referenceElement) + " " + currentPath + " = ");
							converterProviderPrinter.printDomainConverterMethodName(context.getConfigurationTypeElement().getConverterTypeElement(), domainGetterMethod.getReturnType(), pw);
							pw.println(".getDomainInstance(" + DTO_NAME + "." + methodHelper.toGetter(fullPath + methodHelper.toMethod(methodHelper.toField(toHelper.getIdMethod((DeclaredType)domainGetterMethod.getReturnType(), entityResolver)))) + ");");
							instances.add(fullPath);
						} else {
							pw.println(nameTypesUtils.toType(referenceElement) + " " + currentPath + " = " + RESULT_NAME + "." + methodHelper.toGetter(currentPath) + ";");
							pw.println("if (" + RESULT_NAME + "." + methodHelper.toGetter(currentPath) + " == null) {");
							pw.print(currentPath + " = ");
							converterProviderPrinter.printDomainConverterMethodName(context.getConfigurationTypeElement().getConverterTypeElement(), domainGetterMethod.getReturnType(), pw);
							pw.println(".createDomainInstance(null);");
							instances.add(fullPath);
							pw.println("}");
							
						}
					}
					
					if (instances.contains(currentPath)) {
						pw.println(previousPath + "." + methodHelper.toSetter(currentPath) + "(" + currentPath + ");");
					}
				}

				previousPath = currentPath;
				currentPath = pathResolver.next();
				fullPath += methodHelper.toMethod(currentPath);
			}

			if (domainTypeElement != null && toHelper.getDomainSetterMethod(domainTypeElement, context.getDomainFieldPath()) != null) {
				if (context.getConverterType() != null) {
					String converterName = "converter" + methodHelper.toMethod("", context.getFieldName());
					pw.print(context.getConverterType().toString(ClassSerializer.CANONICAL, true) + " " + converterName + " = ");
					converterProviderPrinter.printDomainConverterMethodName(context.getConverterType(), context.getDomainMethodReturnType(), pw);
					pw.println(";");
					pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getDomainFieldPath()) + "("+ converterName + ".fromDto(");
					pw.print("(" + castToDelegate(context.getDomainMethodReturnType()).toString(ClassSerializer.CANONICAL, true) + ")");
					pw.print(DTO_NAME  + "." + methodHelper.toGetter(context.getFieldName()));
				} else {
					pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getDomainFieldPath()) + "(" + DTO_NAME + "." + methodHelper.toGetter(context.getFieldName()));
				}
				if (context.getConverterType() != null) {
					pw.print(")");
				}
				pw.println(");");
			} else {
				ExecutableElement domainGetterMethod = domainTypeElement ==null ? null : toHelper.getDomainGetterMethod(domainTypeElement, currentPath);
				
				VariableElement field = methodHelper.getField(element, currentPath);
				
				if ((domainGetterMethod == null && field != null && !entityResolver.isIdField(field)) || !entityResolver.isIdMethod(domainGetterMethod)) {
					processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Setter is not available for the field " + currentPath + " in the class " + element.toString(), context.getConfigurationTypeElement().asElement());
				}
			}
		} else {
			boolean generated = true;
			if (context.getConverterType() != null) {
				String converterName = "converter" + methodHelper.toMethod("", context.getFieldName());
				pw.print(context.getConverterType().toString(ClassSerializer.CANONICAL, true) + " " + converterName + " = ");
				converterProviderPrinter.printDomainConverterMethodName(context.getConverterType(), context.getDomainMethodReturnType(), pw);
				pw.println(";");
				pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getDomainFieldPath()) + "(");
				pw.print("(" + castToDelegate(context.getDomainMethodReturnType()).toString(ClassSerializer.CANONICAL, true) + ")");
				pw.print(converterName + ".fromDto(" + DTO_NAME  + "." + methodHelper.toGetter(context.getFieldName()));
			} else if (context.getLocalConverterName() != null) {
				pw.println("if (" + context.getLocalConverterName() + " != null) {");
				pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getDomainFieldPath()) + "(" + 
						context.getLocalConverterName() + ".fromDto(" + DTO_NAME  + "." + methodHelper.toGetter(context.getFieldName()) + ")");
			} else {
				ExecutableElement domainGetterMethod = domainTypeElement == null ? null : toHelper.getDomainGetterMethod(domainTypeElement, currentPath);
				
				if (toHelper.getDomainSetterMethod(domainTypeElement, context.getDomainFieldPath()) != null) {
					pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getDomainFieldPath()) + "(" + DTO_NAME  + "." + methodHelper.toGetter(context.getFieldName()));
				} else {
					
					VariableElement field = methodHelper.getField(domainTypeElement, currentPath);
					
					if ((domainGetterMethod == null && field != null && !entityResolver.isIdField(field)) || !entityResolver.isIdMethod(domainGetterMethod)) {
						processingEnv.getMessager().printMessage(Kind.ERROR, "[ERROR] Setter is not available for the field " + currentPath + " in the class " + element.toString(), context.getConfigurationTypeElement().asElement());
						generated = false;
					}
				}
			}
			
			if (generated) {
				if (context.getConverterType() != null) {
					pw.print(")");
				}
				pw.println(");");
			}

			if (context.getLocalConverterName() != null) {
				pw.println("} else {");
				pw.print(RESULT_NAME + "." + methodHelper.toSetter(context.getDomainFieldPath()));
				if (context.getDomainMethodReturnType().getKind().equals(TypeKind.TYPEVAR)) {
					pw.print("((" + ConverterTypeElement.DOMAIN_TYPE_ARGUMENT_PREFIX + "_" + ((TypeVariable)context.getDomainMethodReturnType()).asElement().getSimpleName().toString() + ")");
				} else {
					pw.print("((" + context.getDomainMethodReturnType() + ")");
				}
				pw.print(DTO_NAME  + "." + methodHelper.toGetter(context.getFieldName()));
				pw.println(");");
				pw.println("}");
			}
		}
	}
}